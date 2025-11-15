import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import "../../App.css"
import "../../static/css/game/create/create.css";

export default function CreateGame() {
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();
  const [numPlayers, setnumPlayers] = useState(4);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [isPublic, setIsPublic] = useState(true);

  const MIN = 2, MAX = 5;
  const dec = () => setnumPlayers((n) => Math.max(MIN, n - 1));
  const inc = () => setnumPlayers((n) => Math.min(MAX, n + 1));

  async function handleCreate(e) {
    e.preventDefault();
    setMessage(null);
    setLoading(true);
    try {
      const resp = await fetch("/api/v1/games", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${jwt}`,
        },
        body: JSON.stringify({ numPlayers, isPublic }),
      });
      const json = await resp.json();
      if (!resp.ok) {
        throw new Error(json.message || "Failed to create game");
      }
      navigate(`/games/${json.id}`, { state: json });
    } catch (err) {
      setMessage(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="create">
      <section className="create__hero">
        <h1 className="title-xl">CREATE&nbsp;GAME</h1>
        <div className="create__players">
          <div className="create__playersLabel">NUMBER OF<br/>PLAYERS</div>
          <div className="create__playersControl" role="group" aria-label="Select number of players">
            <button
              type="button"
              className="btn"
              aria-label="Decrease players"
              onClick={dec}
              disabled={numPlayers === MIN}
            >
              –
            </button>

            <div className="create__playersValue" aria-live="polite">{numPlayers}</div>

            <button
              type="button"
              className="btn"
              aria-label="Increase players"
              onClick={inc}
              disabled={numPlayers === MAX}
            >
              +
            </button>
            



          </div>
          <label className="create__playersLabel">
            <input
              type="checkbox"
              checked={isPublic}
              onChange={(e) => setIsPublic(e.target.checked)}
            />
            PUBLIC GAME
          </label>
        </div>
      </section>

      <section className="create__footer">
        <form onSubmit={handleCreate} className="create__form">
          <button className="btn" disabled={loading}>
            {loading ? "CREATING..." : "CREATE"}
          </button>
          {message && <div className="create__error">{message}</div>}
        </form>

        <p className="create__credits">Developed by L3-04</p>
      </section>
    </main>
  );
}