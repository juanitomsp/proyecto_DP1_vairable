import React, { useState } from "react";
import { useLocation, useParams, useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import "../../static/css/auth/authPage.css";


export default function GameWaitingRoom() {
  const { id } = useParams();
  const jwt = tokenService.getLocalAccessToken();
  const location = useLocation();
  const navigate = useNavigate();
  const [copyNotice, setCopyNotice] = useState(null);


  const [game, setGame] = useFetchState(
    location?.state || {},
    `/api/v1/games/${id}`,
    jwt,
    () => {},
    () => {},
    id
  );


  const inviteCode = game?.inviteCode || "";
  const numPlayers = game?.numPlayers || "";
  const status = game?.status || "NEW";
  const isPublic = game?.isPublic;
  let tipo = "";
  if (isPublic == true) {
    tipo = "Public";
  } else {
    tipo = "Private";
  }


  async function copyCode() {
    try {
      await navigator.clipboard.writeText(inviteCode);
      setCopyNotice("Invite code copied!");
      setTimeout(() => setCopyNotice(null), 2000);
    } catch {
      setCopyNotice("Failed to copy");
      setTimeout(() => setCopyNotice(null), 3000);
    }
  }

  function handleLeave() {
    navigate("/"); 
  }


  const shareUrl = `${window.location.origin}/games/${id}`;


  return (
    <div className="auth-page-container">
      <div className="auth-form-container" style={{ maxWidth: 640, width: "100%" }}>
        <h1 style={{ fontSize: 100 }}>Game Waiting Room</h1>
        <div style={{ marginTop: 10 }}>
          <div><b>Game ID:</b> {id}</div>
          <div><b>Status:</b> {status}</div>
          <div><b>Number of players:</b> {numPlayers}</div>
          <div>{tipo}</div>
          <div style={{ marginTop: 10 }}>
            <b>Invite code:</b>{" "}
            <span style={{ fontFamily: "monospace" }}>{inviteCode}</span>
            <button className="btn" style={{ marginLeft: 10 }} onClick={copyCode}>
              Copy
            </button>
            {copyNotice && (
              <div
                aria-live="polite"
                role="status"
                style={{ marginTop: 8, fontSize: 14, color: "#23c483" }}
              >
                {copyNotice}
              </div>
            )}
          </div>
          <div style={{ marginTop: 10 }}>
            <b>Share link:</b>{" "}
            <span style={{ fontFamily: "monospace" }}>{shareUrl}</span>
          </div>


          <div style={{ marginTop: 20 }}>
            <button className="btn" disabled>Start </button>
            <button className="btn" style={{ marginLeft: 10 }} onClick={handleLeave}>
              Leave
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
