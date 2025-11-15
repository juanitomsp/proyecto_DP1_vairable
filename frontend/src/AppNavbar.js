import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import jwt_decode from "jwt-decode";
import tokenService from "./services/token.service";

import "./App.css";
import logo from "./static/images/Group 74.png";

const IconUser = (props) => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" {...props}>
    <circle cx="12" cy="8" r="4" stroke="currentColor" strokeWidth="2" />
    <path d="M4 20c0-4 4-6 8-6s8 2 8 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
  </svg>
);
const IconGlobe = (props) => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" {...props}>
    <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="2" />
    <path d="M3 12h18M12 3c3.5 4.2 3.5 13.8 0 18M12 3c-3.5 4.2-3.5 13.8 0 18" stroke="currentColor" strokeWidth="2" />
  </svg>
);

function SlashNav({ items }) {
  return (
    <nav className="home__nav" aria-label="Main">
      {items.map((it, i) => (
        <React.Fragment key={i}>
          {it.children ? (
            <div className="nav-item has-submenu">
              <button type="button" className="nav-label" aria-haspopup="true" aria-expanded="false">
                {it.label} <span className="caret">▾</span>
              </button>
              <div className="submenu" role="menu">
                {it.children.map((child, idx) => (
                  <Link key={idx} to={child.to} role="menuitem">{child.label}</Link>
                ))}
              </div>
            </div>
          ) : (
            <Link className="nav-item" to={it.to}>{it.label}</Link>
          )}
          {i < items.length - 1 && <span className="slash">/</span>}
        </React.Fragment>
      ))}
    </nav>
  );
}

export default function AppNavbar() {
  const jwt = tokenService.getLocalAccessToken();
  const loggedIn = Boolean(jwt);

  const [roles, setRoles] = useState([]);
  useEffect(() => {
    if (jwt) {
      const decoded = jwt_decode(jwt);
      setRoles(decoded.authorities || []);
    } else setRoles([]);
  }, [jwt]);

  let centerLinks = [];
  if (roles.includes("ADMIN")) {
    centerLinks = [
      { label: "USERS",        to: "/users" },
      { label: "ACHIEVEMENTS", to: "/achievements" },
      { label: "MATCHES",      to: "/matches" },
      { label: "PRICING",      to: "/plans" },
    ];
  } else if (roles.includes("PLAYER")) {
    centerLinks = [
      {
        label: "GAME",
        children: [
          { label: "NEW GAME",  to: "/create-game" },
          { label: "JOIN GAME", to: "/join-game" },
        ],
      },
      {
        label: "RESULTS",
        children: [
          { label: "ACHIEVEMENTS", to: "/achievements" },
          { label: "STATISTICS",   to: "/statistics" },
          { label: "FINISHED MATCHES", to: "/matches" },
        ],
      },
      { label: "FRIENDS", to: "/games/friends" },
    ];
  } else {
    centerLinks = [
      { label: "PRICING",  to: "/plans" },
      { label: "REGISTER", to: "/register" },
      { label: "LOG IN",   to: "/login" },
    ];
  }

  const profileHref = loggedIn ? "/profile" : "/login?return=/profile";

  return (
    <header className="home__topbar">
      <div className="home-logo">
        <a
          href="/"
          className="home-logo"
          aria-label="Reload home"
          onClick={(e) => { e.preventDefault(); window.location.href = "/"; }}
        >
          <img src={logo} alt="Upstream logo" className="home__logo" />
        </a>
      </div>

      <SlashNav items={centerLinks} />

      <div className="home__actions">
        <Link className="icon-btn" to={profileHref} aria-label="Profile" title="Profile">
          <IconUser />
        </Link>
        <a className="icon-btn" href="/language" aria-label="Language" title="Language">
          <IconGlobe />
        </a>
      </div>
    </header>
  );
}