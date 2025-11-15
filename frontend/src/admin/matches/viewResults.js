import React, { useEffect, useState } from "react";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authPage.css"; 
import {Link, useParams } from "react-router-dom";


const jwt = tokenService.getLocalAccessToken();
export default function ViewResults({ game }) {
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState(null);
  const [player, setPlayers] = useState([])
  const imgCrown = "https://cdn-icons-png.flaticon.com/512/6941/6941697.png"
  const params = useParams()
  const maxPoints = Math.max(...player.map(p => p.points));


  useEffect(() => {
    console.log(params)
    async function fetchResults() {
        try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        };
        const resp = await fetch(`/api/v1/games/${params.gameId}/players`, { headers });
        if (!resp.ok) {
          let detail = "";
          try {
            const json = await resp.json();
            detail = json?.message || json?.error || "";
          } catch (err) { 
          throw new Error(`Failed to fetch results (HTTP ${resp.status}) ${detail}`);
          }
        }
        const data = await resp.json();
        console.log(data);
        setPlayers(data);
      } catch (err) {
        setMessage(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchResults();
        }, [params]);
    return(
        <div className = "auth-page-container">
            <div className="auth-form-container"
                style={{
                maxWidth: 600,
                width: "100%",
                textAlign: "center",
                overflowX: "auto",
              }}>
            <h1 style={{ marginBottom: 20 }}>View Results</h1>

              {loading ? (
                <p>Loading...</p>
              ) : message ? (<p style={{ color: "#cc0033" }}>{message}</p>
        ) : (
            <table style={{
              width: "100%",
              borderCollapse: "collapse",
              fontSize: 15,
            }}
          >
            <thead>
              <tr style={{ backgroundColor: "#222", color: "#fff" }}>
                <th style={thStyle}>Players</th>
                <th style={thStyle}>Emails</th>
                <th style={thStyle}>Total Points</th>
              </tr>
            </thead>
            <tbody>

          {player.map((p) => (
            <tr key={p.id}>
            <td style={tdStyle}> {p.user.username}</td>
            <td style={tdStyle}>{p.user.email}</td>
             <td style={tdStyle}>{p.points} Points
                  {p.points === maxPoints && <img src={imgCrown} alt="Crown" style={{ width: '20px', height: '30px', marginLeft: '10px' }} />}
            </td>
            </tr>))}

            </tbody>
          </table>
        )}
        <Link to={`/matches`}>
        <button
        className="auth-button"
        style={diseñoBoton}>
          Back to matches
        </button>
       </Link>
            </div>
        </div>
    )
}
const thStyle = { 
  padding: "10px 8px", 
  borderBottom: "2px solid #444" 
};
const tdStyle = { 
  padding: "8px", 
  textAlign: "center" 
};
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
