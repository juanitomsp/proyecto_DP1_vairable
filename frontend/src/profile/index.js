import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import userService from "../services/userService";
import "../static/css/profile/profile.css";

export default function Profile() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(true);

  const handleEdit = () => navigate("/profile/editProfile");
  const handleLogOut = () => navigate("/logout");

  useEffect(() => {
    let active = true;
    (async () => {
      try {
        const res = await userService.getCurrentUser();
        if (!active) return;
        setUser(res.data);
      } catch (e) {
        if (!active) return;
        setErr(e.response?.data?.message || e.message);
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    })();
    return () => { active = false; };
  }, []);

  if (loading) {
    return (
      <main className="profile">
        <header className="page-header">
          <h1 className="title-xl">MY PROFILE</h1>
        </header>
        <section className="profile__panel">
          <div className="profile__loading">Loading…</div>
        </section>
      </main>
    );
  }

  if (err) {
    return (
      <main className="profile">
        <header className="page-header">
          <h1 className="title-xl">MY PROFILE</h1>
        </header>
        <section className="profile__panel">
          <div className="profile__error">{err}</div>
          <div className="profile__actions">
            <button className="btn btn-block" onClick={handleLogOut}>LOG OUT</button>
          </div>
        </section>
      </main>
    );
  }


  return (
    <main className="profile">
      <header className="page-header">
        <h1 className="title-xl">MY PROFILE</h1>
      </header>

      <section className="profile__panel">
        {user ? (
          <>
            <div className="profile__info">
              <div className="profile__row">
                <div className="profile__dt">Username:</div>
                <div className="profile__dd">{user.username}</div>
              </div>
              <div className="profile__row">
                <div className="profile__dt">Email:</div>
                <div className="profile__dd">{user.email}</div>
              </div>
              <div className="profile__row">
                <div className="profile__dt">Role:</div>
                <div className="profile__dd">{user.authority}</div>
              </div>
            </div>

            <div className="profile__actions">
              <button className="btn btn-block" onClick={handleEdit}>EDIT</button>
              <button className="btn btn-block" onClick={handleLogOut}>LOG OUT</button>
            </div>
          </>
        ) : (
          <div className="profile__error">Could not load user data.</div>
        )}
      </section>
    </main>
  );
}