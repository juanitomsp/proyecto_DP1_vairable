import React, { useEffect, useState } from "react";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authPage.css"; 
import "../../static/css/admin/partidas.css";
import { Link } from "react-router-dom";
import "../../static/css/admin/botonMatches.css";

export default function FinishedMatches() {
  const jwt = tokenService.getLocalAccessToken();
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState(null);
  const [selectedGame, setSelectedGame] = useState(null);

  const imagePublic = "https://cdn-icons-png.flaticon.com/512/17717/17717799.png";
  const imageNotPublic = "https://cdn-icons-png.flaticon.com/512/9502/9502767.png";

  useEffect(() => {
    let mounted = true;
    async function fetchGames() {
      setLoading(true);
      setMessage(null);
      try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        };
        const resp = await fetch("/api/v1/games", { headers });
        if (!resp.ok) {
          let detail = "";
          try { const json = await resp.json(); detail = json?.message || json?.error || ""; } catch (e) {}
          throw new Error(`Failed to fetch games (HTTP ${resp.status}) ${detail}`);
        }
        const data = await resp.json();
        if (mounted) setGames(data);
      } catch (err) {
        if (mounted) setMessage(err.message);
      } finally {
        if (mounted) setLoading(false);
      }
    }
    fetchGames();
    return () => { mounted = false; };
  }, [jwt]);

  const handleGameSelect = (game) => setSelectedGame(game);

  const finished = (games || []).filter(g => g.status === "FINISHED");

  return (
    <div className="auth-page-container">
      <div className="auth-form-container" style={{ maxWidth: 720, width: "100%", textAlign: "center", overflowX: "auto" }}>
        <h2 className="title-lg" style={{ marginBottom: 20 }}>Finished Matches</h2>

        {loading ? (
          <p>Loading games ...</p>
        ) : message ? (
          <p style={{ color: "#cc0033" }}>{message}</p>
        ) : finished.length === 0 ? (
          <p>No games found.</p>
        ) : (
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 15 }}>
            <thead>
              <tr style={{ backgroundColor: "#222", color: "#fff" }}>
                <th style={thStyle}>Code</th>
                <th style={thStyle}>Players</th>
                <th style={thStyle}>Create</th>
                <th style={thStyle}>Status</th>
                <th style={thStyle}>ownerId</th>
              </tr>
            </thead>
            <tbody>
              {finished.map((g) => (
                <tr key={g.id} onClick={() => handleGameSelect(g)} className={'partida'}>
                  <td style={tdStyle}>{g.inviteCode}</td>
                  <td style={tdStyle}>{g.numPlayers}</td>
                  <td style={tdStyle}>{g.createdAt}</td>
                  <td style={tdStyle}><span style={{ color: "#5cb85c", fontWeight: "bold" }}>Finished</span></td>
                  <td style={tdStyle}>{g.ownerId}</td>
                  <td style={tdStyle}>
                    <img src={g.isPublic ? imagePublic : imageNotPublic} alt={g.numPlayers} width="30px" />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {selectedGame && (
          <Link to={`/matches/${selectedGame.id}/viewResults`}>
            <button className="auth-button" style={diseñoBoton}>View Results</button>
          </Link>
        )}
      </div>
    </div>
  );
}

const thStyle = { padding: "10px 8px", borderBottom: "2px solid #444" };
const tdStyle = { padding: "8px", textAlign: "center" };
const diseñoBoton = {
  backgroundColor: '#12443fff',
  color: '#ffffffff',
  padding: '15px 32px',
  fontSize: '16px',
  borderRadius: '60px',
  marginBottom: "10px",
  cursor: 'pointer',
  border: '3px solid white'
};