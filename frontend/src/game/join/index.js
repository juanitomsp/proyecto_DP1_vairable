import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import "../../static/css/game/join/join.css";
import "../../static/css/auth/authPage.css";

export default function JoinGame() {
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();

  const [inviteCode, setInviteCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [publicGames, setPublicGames] = useState([]);

  useEffect(() => {
    async function fetchPublicGames() {
      try {
        const resp = await fetch("/api/v1/games/public/unfinished", {
          headers: { Authorization: `Bearer ${jwt}` },
        });
        const json = await resp.json();
        if (!resp.ok) throw new Error(json.message || "No public games found");
        setPublicGames(json);
      } catch (err) {
        setMessage(err.message);
      }
    }
    fetchPublicGames();
  }, [jwt]);

  async function handleJoinPublic(gameId) {
    setMessage(null);
    setLoading(true);
    try {
      const joinResp = await fetch(`/api/v1/games/${gameId}/join`, {
        method: "GET",
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const joinJson = await joinResp.json();
      if (!joinResp.ok) throw new Error(joinJson.message || "Failed to join game");
      navigate(`/games/${gameId}`, { state: joinJson });
    } catch (err) {
      setMessage(err.message);
    } finally {
      setLoading(false);
    }
  }
  async function handleJoinSpectator(gameId) {
  setMessage(null);
  setLoading(true);
  try {
    const resp = await fetch(`/api/v1/games/${gameId}/spectate`, {
      method: "GET",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    const json = await resp.json();
    if (!resp.ok) throw new Error(json.message || "Failed to join as spectator");

    navigate(`/games/${gameId}`, { state: json });
  } catch (err) {
    setMessage(err.message);
  } finally {
    setLoading(false);
  }
}

  async function handleJoinPrivate(e) {
    e.preventDefault();
    if (!inviteCode) return;

    setLoading(true);
    setMessage(null);

    try {
      const resp = await fetch(`/api/v1/games/invite/${inviteCode}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const json = await resp.json();
      if (!resp.ok) throw new Error(json.message || "Invalid invite code");

      const gameId = json.id;
      const joinResp = await fetch(`/api/v1/games/${gameId}/join`, {
        method: "GET",
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const joinJson = await joinResp.json();
      if (!joinResp.ok) throw new Error(joinJson.message || "Failed to join game");

      navigate(`/games/${gameId}`, { state: joinJson });
    } catch (err) {
      setMessage(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (

      <div className="join-page-container">
        <h1 className="title-lg">JOIN A GAME</h1>
        <div className="join-form-container join__wrap">
          <div className="join__columns">
            <div className="join__public">
              <div className="title-lg" style={{ fontSize: 28 }}>PUBLIC GAMES</div>

              {publicGames.length === 0 ? (
                <p>No public games available.</p>
              ) : (
                <ul
                  className={`join__list ${
                    publicGames.length > 2 ? "join__list--limit" : ""
                  }`}
                  aria-label="public games"
                >
                  {publicGames.map((g) => (
                    <li key={g.id} className="join__card">
                      <div className="join__card-line">
                        <span className="join__card-title">Game {g.id}</span>
                        <span>{g.numPlayers} players</span>
                      </div>
                      {g.inviteCode && (
                        <div className="join__card-line" style={{ fontFamily: "monospace" }}>
                          <span>Code: {g.inviteCode}</span>
                          <span>{g.status}</span>
                        </div>
                      )}
                      <div className="join__card-line">
                      {(() => {
                        const isWaiting = g.status === "WAITING";
                        const isStarted = g.status === "PLAYING";
                        const label = isWaiting ? "JOIN" : isStarted ? "JOIN AS SPECTATOR" : "UNAVAILABLE";
                        const disabled = loading || (!isWaiting && !isStarted);

                        return (
                          <button
                            className="btn"
                            onClick={() =>
                              isWaiting ? handleJoinPublic(g.id) : isStarted ? handleJoinSpectator(g.id) : null
                            }
                            disabled={disabled}
                          >
                            {label}
                          </button>
                        );
                      })()}
                    </div>
                    </li>
                  ))}
                </ul>
              )}
            </div>

            <div className="join__private">
              <div className="title-lg" style={{ fontSize: 28 }}>PRIVATE GAMES</div>

              <form onSubmit={handleJoinPrivate} className="join__form">
                <div className="form-field" style={{ marginTop: 8 }}>
                  <label className="label" htmlFor="inviteCode">ENTER INVITE CODE</label>
                  <input
                    type="text"
                    id="inviteCode"
                    className="input"
                    placeholder="e.g. ABC123"
                    value={inviteCode}
                    onChange={(e) => setInviteCode(e.target.value.trim().toUpperCase())}
                    maxLength={8}
                    required
                  />
                </div>

                <button
                  className="btn btn-lg"
                  type="submit"
                  disabled={loading || inviteCode.length < 4}
                  style={{ marginTop: 12 }}
                >
                  {loading ? "JOINING..." : "JOIN"}
                </button>

                {message && <div className="join__error" role="alert">{message}</div>}
              </form>
            </div>
          </div>
        </div>
      </div>

  );
}