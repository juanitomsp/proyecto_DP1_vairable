import React, { useEffect, useState } from "react";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authPage.css"; 
import "../../static/css/admin/partidas.css";
import {Link} from "react-router-dom";
import "../../static/css/admin/botonMatches.css";
import candado_abierto from "../../static/images/candado abierto blanco.png";
import candado_cerrado from "../../static/images/candado cerrado.png";

const jwt = tokenService.getLocalAccessToken();
export default function AdminGamesList() {
  const [games, setGames] = useState([]);

  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState(null);
  const [selectedGame, setSelectedGame] = useState(null);
  const [view, setView] = useState(null);


  useEffect(() => {
    async function fetchGames() {
      try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        };

        const resp = await fetch("/api/v1/games", { headers });
        if (!resp.ok) {
          let detail = "";
          try {
            const json = await resp.json();
            detail = json?.message || json?.error || "";
          } catch (err) { 
          throw new Error(`Failed to fetch games (HTTP ${resp.status}) ${detail}`);
          }
        }

        const data = await resp.json();
        setGames(data);
      } catch (err) {
        setMessage(err.message);
      } finally {
        setLoading(false);
      }
    }
    if (view !== null){
      setSelectedGame(null);
    }
    fetchGames();
  }, [view]);

  const handleGameSelect = (game) => {
    setSelectedGame(game)
  }
  
  if (view === null){
  return (
    <div className="auth-page-container">
      <div
        className="auth-form-container"
        style={{
          maxWidth: 720,
          width: "100%",
          textAlign: "center",
          overflowX: "auto",
        }}
      >
        <h2 className="title-lg"
        style={{ marginBottom: 20 }}>Matches</h2>

        {loading ? (
          <p>Loading ...</p>
        ) : message ? (
          <p style={{ color: "#cc0033" }}>{message}</p>
        ) : games.length === 0 ? (
          <p>No data found.</p>
        ) : (
          <div className="botonMatches-container">
           <button
                className="botonMatches"
                onClick={() => setView("active")}>
                  <h4 className="botonMatches-title">
                   See list of active matches
                 </h4>
                  <h5 className="botonMatches-description">
                  Number of matches active: {games.filter((g) => g.status !== "FINISHED").length}
                  </h5> <h5 className="botonMatches-description">
                  Number of matches public: {games.filter((g) => g.isPublic && g.status !== "FINISHED").length}
                  </h5><h5 className="botonMatches-description">
                  Number of matches not public: {games.filter((g) => !g.isPublic && g.status !== "FINISHED").length}
                  </h5>
            </button>
            <button
                className="botonMatches"
                onClick={() => setView("finished")}>
                  <h4 className="botonMatches-title">
                   See list of finished matches
                 </h4>
                  <h5 className="botonMatches-description">
                  Number of matches finished: {games.filter((g) => g.status === "FINISHED").length} 
                  </h5><h5 className="botonMatches-description">
                  Number of matches public: {games.filter((g) => g.isPublic && g.status === "FINISHED").length}
                  </h5><h5 className="botonMatches-description">
                  Number of matches not public: {games.filter((g) => !g.isPublic && g.status === "FINISHED").length}
                  </h5>
            </button>
             </div>
     
        )}
          
      </div>
    </div>
  )
}
  else if (view === "active"){
    return (
    <div className="auth-page-container">
      <div
        className="auth-form-container"
        style={{
          maxWidth: 720,
          width: "100%",
          textAlign: "center",
          overflowX: "auto",
        }}
      >
        <h2 className="title-lg"
        style={{ marginBottom: 20 }}>Active Matches</h2>

        {loading ? (
          <p>Loading games ...</p>
        ) : message ? (
          <p style={{ color: "#cc0033" }}>{message}</p>
        ) : games.length === 0 ? (
          <p>No games found.</p>
        ) : (
           <table
            style={{
              width: "100%",
              borderCollapse: "collapse",
              fontSize: 15,
            }}
          >
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
              {games.filter(g =>  g.status !== "FINISHED").map((g) => (
                <tr key={g.id} 
                onClick={()=> handleGameSelect(g)}
                className={'partida'}
                >
                  <td style={tdStyle}>{g.inviteCode}</td>
                  <td style={tdStyle}>{g.numPlayers}</td>
                  <td style={tdStyle}>{g.createdAt}</td>
                  
                  
                  <td style={tdStyle}>
                    {g.status === "WAITING" && (
                      <span style={{ color: "#f0ad4e", fontWeight: "bold" }}>
                        Waiting
                      </span>
                    )}
                    {g.status === "PLAYING" && (
                      <span style={{ color: "#0275d8", fontWeight: "bold" }}>
                        Playing
                      </span>
                    )}
                    
                  </td>
                  <td style={tdStyle}>{g.ownerId}</td>
                  <td style={ tdStyle}>
                    <img src = {g.isPublic? candado_abierto: candado_cerrado} alt={g.numPlayers} width="30px"/>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )} 
        {selectedGame && (
            <div style= {{marginTop:20}}>
              <button
                className="auth-button"
                style={diseñoBoton}>
                Join
              </button>
            </div>
        )}
            <div style= {{marginTop:20}}>
              <button
                className="auth-button"
                style={diseñoBoton}
                onClick={() => setView(null)}>
                Back to All Matches
              </button>
            </div>
      </div>
    </div>
  );
} else if (view === "finished"){
    return (
    <div className="auth-page-container">
      <div
        className="auth-form-container"
        style={{
          maxWidth: 720,
          width: "100%",
          textAlign: "center",
          overflowX: "auto",
        }}
      >
        <h2 className="title-lg"
        style={{ marginBottom: 20 }}>Finished Matches</h2>

        {loading ? (
          <p>Loading games ...</p>
        ) : message ? (
          <p style={{ color: "#cc0033" }}>{message}</p>
        ) : games.length === 0 ? (
          <p>No games found.</p>
        ) : (
           <table
            style={{
              width: "100%",
              borderCollapse: "collapse",
              fontSize: 15,
            }}
          >
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
              {games.filter(g => g.status === "FINISHED").map((g) => (
                <tr key={g.id} 
                onClick={()=> handleGameSelect(g)}
                className={'partida'}
                >
                  <td style={tdStyle}>{g.inviteCode}</td>
                  <td style={tdStyle}>{g.numPlayers}</td>
               
                  <td style={tdStyle}>{g.createdAt}</td>
                  <td style={tdStyle}>
                    <span style={{ color: "#5cb85c", fontWeight: "bold" }}>
                        Finished
                      </span>
                  </td>
                  <td style={tdStyle}>{g.ownerId}</td>
                  <td style={ tdStyle}>
                    <img src = {g.isPublic? candado_abierto: candado_cerrado} alt={g.numPlayers} width="30px"/>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
              
        {selectedGame && (
            <Link to={`/matches/${selectedGame.id}/viewResults`}>
              <button
                className="auth-button"
                style={diseñoBoton}>
                View Results
              </button>
            </Link>
        )}
            <div style= {{marginTop:20}}>
              <button
                className="auth-button"
                style={diseñoBoton}
                onClick={() => setView(null)}>
                Back to All Matches
              </button>
            </div>
   
      </div>
    </div>
  );
}
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


