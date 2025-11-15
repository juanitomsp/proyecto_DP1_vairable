import React from "react";
import { Link } from "react-router-dom";
import jwt_decode from "jwt-decode";
import tokenService from "../services/token.service";
import "../App.css";
import "../static/css/home/home.css";
import bg from "../static/images/salmon2.jpeg";

const IconUser = (props) => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" {...props}>
    <circle cx="12" cy="8" r="4" stroke="currentColor" strokeWidth="2"/>
    <path d="M4 20c0-4 4-6 8-6s8 2 8 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
  </svg>
);

const guideUrl = "https://www.youtube.com/watch?v=RARemHyFfRs";

export default function Home() {
  const jwt = tokenService.getLocalAccessToken();
  const loggedIn = Boolean(jwt);

  let roles = [];
  try {
    roles = jwt ? (jwt_decode(jwt).authorities || []) : [];
  } catch { roles = []; }

  const isAdmin  = roles.includes("ADMIN")  || roles.includes("ROLE_ADMIN");
  const isPlayer = roles.includes("PLAYER") || roles.includes("ROLE_PLAYER");
  const isGuest  = !loggedIn;

  const playHref = isPlayer ? "/create-game" : "/login?return=/create-game";
  const joinHref = isPlayer ? "/join-game"   : "/login?return=/join-game";
  const profHref = loggedIn ? "/profile"     : "/login?return=/profile";

  return (
    <main className="home">
      <section className="home__hero" style={{ backgroundImage: `url(${bg})` }}>
        <div className="home__overlay"></div>
        <div className="home__vignette"></div>

        <div className="home__content">
          <div className="home__kicker">THE</div>
          <div className="home__title">UPSTREAM</div>

          {!isAdmin && (
            <div className="home__cta">
              <Link className="btn" to={playHref}>PLAY NOW</Link>
              <Link className="btn btn--ghost" to={joinHref}>JOIN GAME</Link>
            </div>
          )}
        </div>
        <Link to={profHref} className="home__profile">
          PROFILE
          <IconUser className="home__profileIcon" />
        </Link>

        <footer className="home__footer">
          <p className="home__credits">Developed by L3-04 — Dina Samatova, Arancha Aparicio Falcón, Juan María Sánchez Madroñal, Carolina Murillo Gómez, Lucía Torres Benítez, Manuel Rivera Gálvez</p>
          <a
            className="home__guide"
            href={guideUrl}
            target="_blank"
            rel="noopener noreferrer"
          >
            WATCH GUIDE
          </a>
        </footer>
      </section>
    </main>
  );
}