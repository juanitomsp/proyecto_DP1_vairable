import React, { useState } from "react";
import { Table } from "reactstrap";
import { Link } from "react-router-dom";
import jwt_decode from "jwt-decode";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import deleteFromList from "../util/deleteFromList";
import getErrorModal from "../util/getErrorModal";

import "../static/css/achievements/achievements.css";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

export default function AchievementList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  let roles = [];
    try {
      roles = jwt ? (jwt_decode(jwt).authorities || []) : [];
    } catch { roles = []; }

  const isAdmin  = roles.includes("ADMIN")  || roles.includes("ROLE_ADMIN");
  const isPlayer = roles.includes("PLAYER") || roles.includes("ROLE_PLAYER");
  const [achievements, setAchievements] = useFetchState(
    [],
    `/api/v1/achievements`,
    jwt,
    setMessage,
    setVisible
  );

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <main className="achievements-page">
      <header className="achievements-header">
        <h1 className="title-xl">ACHIEVEMENTS</h1>
      </header>

      {alerts.map((a) => a.alert)}
      {modal}

      <section className="achievements-panel">
        <Table aria-label="achievements" className="achievements-table">
          <thead>
            <tr>
              <th className="text-center">Name</th>
              <th className="text-center">Description</th>
              <th className="text-center">Image</th>
              <th className="text-center">Threshold</th>
              <th className="text-center">Metric</th>
               {isAdmin && (
              <th className="text-center">Actions</th>
               )}
            </tr>
          </thead>
          <tbody>
            {achievements.map((a) => (
              <tr key={a.id}>
                <td className="text-center" style={{color:'white'}}>{a.name}</td>
                <td className="text-center" style={{color:'white'}}>{a.description}</td>
                <td className="text-center" style={{color:'white'}}>
                  <img
                    src={a.badgeImage ? a.badgeImage : imgnotfound}
                    alt={a.name}
                    width="50"
                    height="50"
                    style={{ objectFit: "cover", borderRadius: 8 }}
                  />
                </td>
                <td className="text-center" style={{color:'white'}}>{a.threshold}</td>
                <td className="text-center" style={{color:'white'}}>{a.metric}</td>
                <td className="text-center">
                   {isAdmin && (
                  <div className="achievements-actions">
                    <Link
                      to={a.id != null ? `/achievements/${a.id}` : "#"}
                      className="btn"
                      onClick={(e) => {
                        if (a.id == null) {
                          e.preventDefault();
                          alert("No id for this achievement");
                        }
                      }}
                    >
                      EDIT
                    </Link>
                    <button
                      className="btn btn-danger"
                      onClick={() =>
                        deleteFromList(
                          `/api/v1/achievements/${a.id}`,
                          a.id,
                          [achievements, setAchievements],
                          [alerts, setAlerts],
                          setMessage,
                          setVisible
                        )
                      }
                    >
                      DELETE
                    </button>
                  </div>
                   )}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        {isAdmin && (
        <div className="achievements-actions">
          <Link to="/achievements/new" className="btn">CREATE ACHIEVEMENT</Link>
        </div>
        )}
      </section>
    </main>
  );
}