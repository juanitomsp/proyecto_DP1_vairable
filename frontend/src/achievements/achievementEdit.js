import React, { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import { Link, useNavigate } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "../util/getErrorModal";
import getIdFromUrl from "../util/getIdFromUrl";
import "../static/css/achievements/achievements.css";

const jwt = tokenService.getLocalAccessToken();

export default function AchievementEdit() {
  const rawId = getIdFromUrl(2);
  const isNumericId = /^\d+$/.test(String(rawId || ""));
  const id = isNumericId ? Number(rawId) : null;

  const [achievement, setAchievement] = useState({
    id,
    name: "",
    description: "",
    badgeImage: "",
    threshold: 1,
    metric: "GAMES_PLAYED",
    actualDescription: "",
  });

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();

  useEffect(() => {
    if (id == null) return;

    fetch(`/api/v1/achievements/${id}`, {
      headers: { Authorization: `Bearer ${jwt}` },
    })
      .then(async (res) => {
        if (!res.ok) {
          let detail = "";
          try {
            const j = await res.json();
            detail = j?.message || j?.error || "";
          } catch {}
          throw new Error(`Failed to load achievement ${id}. ${detail}`);
        }
        return res.json();
      })
      .then((data) => setAchievement(data))
      .catch((err) => {
        setMessage(err.message);
        setVisible(true);
      });
  }, [id]);

  function handleChange(e) {
    const { name, value } = e.target;
    setAchievement((a) => ({ ...a, [name]: value }));
  }

  function handleSubmit(e) {
    e.preventDefault();

    fetch(
      "/api/v1/achievements" + (achievement.id != null ? `/${achievement.id}` : ""),
      {
        method: achievement.id != null ? "PUT" : "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(achievement),
      }
    )
      .then((r) => r.text())
      .then((t) => {
        if (t === "") return navigate("/achievements");
        const j = JSON.parse(t);
        if (j?.message) {
          setMessage(j.message);
          setVisible(true);
        } else {
          navigate("/achievements");
        }
      })
      .catch((err) => alert(err));
  }

  return (
    <main className="achievements-page">
      <header className="achievements-header">
        <h1 className="title-xl">
          {achievement.id != null ? "EDIT ACHIEVEMENT" : "ADD ACHIEVEMENT"}
        </h1>
      </header>

      <section className="achievements-panel">
        {modal}

        <Form className="form" onSubmit={handleSubmit}>
          <div className="form-field">
            <Label for="name" className="label">NAME</Label>
            <Input
              id="name"
              name="name"
              type="text"
              required
              value={achievement.name || ""}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="form-field">
            <Label for="description" className="label">DESCRIPTION</Label>
            <Input
              id="description"
              name="description"
              type="text"
              required
              value={achievement.description || ""}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="form-field">
            <Label for="badgeImage" className="label">BADGE IMAGE URL</Label>
            <Input
              id="badgeImage"
              name="badgeImage"
              type="text"
              required
              value={achievement.badgeImage || ""}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="form-field">
            <Label for="metric" className="label">METRIC</Label>
            <Input
              id="metric"
              name="metric"
              type="select"
              required
              value={achievement.metric || ""}
              onChange={handleChange}
              className="input"
            >
              <option value="">None</option>
              <option value="GAMES_PLAYED">GAMES_PLAYED</option>
              <option value="VICTORIES">VICTORIES</option>
              <option value="TOTAL_PLAY_TIME">TOTAL_PLAY_TIME</option>
            </Input>
          </div>

          <div className="form-field">
            <Label for="threshold" className="label">THRESHOLD VALUE</Label>
            <Input
              id="threshold"
              name="threshold"
              type="number"
              required
              value={achievement.threshold ?? 1}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="achievements-actions">
            <button className="btn" type="submit">SAVE</button>
            <Link to="/achievements" className="btn">CANCEL</Link>
          </div>
        </Form>
      </section>
    </main>
  );
}