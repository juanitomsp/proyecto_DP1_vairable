import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../static/css/profile/editProfile.css";
import userService from "../services/userService";

export default function EditProfile() {
  const navigate = useNavigate();

  const [userId, setUserId] = useState(null);
  const [newUserName, setNewUserName] = useState("");
  const [newMail, setNewMail] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [isSaving, setIsSaving] = useState(false);
  const [saveError, setSaveError] = useState(null);

  useEffect(() => {
    let active = true;
    (async () => {
      try {
        setLoading(true);
        const res = await userService.getCurrentUser(); 
        if (!active) return;

        setNewUserName(res.data.username || "");
        setNewMail(res.data.email || "");
        setUserId(res.data.id); 
        
        setNewPassword(""); 

      } catch (e) {
        if (!active) return;
        setError(e.response?.data?.message || e.message);
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    })();
    return () => { active = false; };
  }, []);

  const handleSave = async (e) => {
    e.preventDefault();
    if (isSaving) return; 

    setIsSaving(true);
    setSaveError(null);

    const userData = {
      username: newUserName,
      email: newMail,
    };

    if (newPassword) {
      userData.password = newPassword;
    }

    try {
      await userService.updateUser(userId, userData);
      
      navigate("/profile");

    } catch (err) {
      setSaveError(err.response?.data?.message || "Error al guardar. Inténtelo de nuevo.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancel = () => navigate("/profile");
  if (loading) {
    return (
      <main className="profile-edit">
        <header className="page-header">
          <h1 className="title-xl">EDIT PROFILE</h1>
        </header>
        <section className="profile-edit__panel">
          <div style={{ padding: "20px", textAlign: "center", color: "white" }}>
            Loading data...
          </div>
        </section>
      </main>
    );
  }

  if (error) {
    return (
      <main className="profile-edit">
        <header className="page-header">
          <h1 className="title-xl">EDIT PROFILE</h1>
        </header>
        <section className="profile-edit__panel">
          <div style={{ padding: "20px", textAlign: "center", color: "red" }}>
            Error: {error}
          </div>
        </section>
      </main>
    );
  }

  return (
    <main className="profile-edit">
      <header className="page-header">
        <h1 className="title-xl">EDIT PROFILE</h1>
      </header>

      <section className="profile-edit__panel">
        <form className="form" onSubmit={handleSave}>

          <div className="form-field">
            <label className="label" htmlFor="userName">USERNAME</label>
            <input
              type="text"
              id="userName"
              className="input"
              value={newUserName}
              onChange={(e) => setNewUserName(e.target.value)}
              placeholder="Your new username"
              required
            />
          </div>

          <div className="form-field">
            <label className="label" htmlFor="mail">EMAIL</label>
            <input
              type="email"
              id="mail"
              className="input"
              value={newMail}
              onChange={(e) => setNewMail(e.target.value)}
              placeholder="name@example.com"
              required
            />
          </div>

          <div className="form-field">
            <label className="label" htmlFor="password">NEW PASSWORD</label>
            <input
              type="password"
              id="password"
              className="input"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Leave blank to keep old password"
            />
          </div>



          <div className="profile-edit__actions">
            <button className="btn" type="submit" disabled={isSaving}>
              {isSaving ? "SAVING..." : "SAVE"}
            </button>
            <button className="btn" type="button" onClick={handleCancel}>CANCEL</button>
          </div>

          {saveError && (
            <div style={{ color: "red", textAlign: "center", marginTop: "10px" }}>
              {saveError}
            </div>
          )}
        </form>
      </section>
    </main>
  );
}