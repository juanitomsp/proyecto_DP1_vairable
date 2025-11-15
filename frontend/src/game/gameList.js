import React, { useState } from "react";
import { Table } from "reactstrap";
import { Link } from "react-router-dom";

import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import deleteFromList from "../util/deleteFromList";
import getErrorModal from "../util/getErrorModal";

import "../static/css/achievements/achievements.css";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

export default function GameList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);

  const [achievements, setAchievements] = useFetchState(
    [],
    `/api/v1/achievements`,
    jwt,
    setMessage,
    setVisible
  );

  const visiblePlayed = (achievements || []).filter(
    (a) => String(a.metric || "").toLowerCase() === "games_played"
  );

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <main className="achievements-page">
      <header className="achievements-header">
        <h1 className="title-xl">GAMES PLAYED</h1>
      </header>

      {alerts.map((a) => a.alert)}
      {modal}
      
      {/*
      {achievements.length === 0 &&(
        <p className="no-achievements" style={{ textAlign: "center", color: "white", marginTop: 20 }}>
          No achievements yet
        </p>
      )}
      {visibleAchievements.length === 0 &&(
        <p className="no-achievements" style={{ textAlign: "center", color: "white", marginTop: 20 }}>
          No achievements yet
        </p>
      )}
      */}

      <section className="achievements-panel">
        <div className="achievements-card">
          <Table aria-label="games" className="achievements-table">
            <thead>
              <tr>
                <th className="text-center">Name</th>
                <th className="text-center">Description</th>
                <th className="text-center">Image</th>
                <th className="text-center">Threshold</th>
                <th className="text-center">Metric</th>
              </tr>
            </thead>
            <tbody>
              {visiblePlayed.map((a) => (
                <tr key={a.id}>
                  <td className="text-center" style={{ color: "white" }}>{a.name}</td>
                  <td className="text-center" style={{ color: "white" }}>{a.description}</td>
                  <td className="text-center" style={{ color: "white" }}>
                    <img
                      src={a.badgeImage ? a.badgeImage : imgnotfound}
                      alt={a.name}
                      width="50"
                      height="50"
                      style={{ objectFit: "cover", borderRadius: 8 }}
                    />
                  </td>
                  <td className="text-center" style={{ color: "white" }}>{a.threshold}</td>
                  <td className="text-center" style={{ color: "white" }}>{a.metric}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </section>
    </main>
  );
}