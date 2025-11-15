import React from "react";
import { FaCheck, FaPaperPlane } from "react-icons/fa";
import { ImAirplane } from "react-icons/im";
import { BsFillRocketTakeoffFill } from "react-icons/bs";
import "../../static/css/pricing/pricingPage.css";

export default function PlanList() {
  return (
    <main className="pricing">
      <header className="pricing-header">
        <h1 className="title-xl">PRICING</h1>
      </header>

      <section className="pricing-grid">
        {/* BASIC */}
        <article className="pricing-card">
          <div className="plan-icon"><FaPaperPlane /></div>
          <div className="plan-name">BASIC</div>
          <div className="plan-price">FREE</div>

          <ul className="feature-list">
            <li className="feature-item">
              <span className="feature-dot">•</span>
              <span>2 games per week</span>
            </li>
          </ul>

          <div className="plan-actions">
            <button className="btn" type="button">CHOOSE</button>
          </div>
        </article>

        {/* GOLD */}
        <article className="pricing-card pricing-card--highlight">
          <div className="plan-icon"><ImAirplane /></div>
          <div className="plan-name">GOLD</div>
          <div className="plan-price">
            5<span>€</span> <small>/ month</small>
          </div>

          <ul className="feature-list">
            <li className="feature-item">
              <span className="feature-dot">•</span>
              <span>4 games per week</span>
            </li>
            <li className="feature-item">
              <FaCheck className="feature-ok" />
              <span>Statistics page</span>
            </li>
          </ul>

          <div className="plan-actions">
            <button className="btn" type="button">CHOOSE</button>
          </div>
        </article>

        {/* PLATINUM */}
        <article className="pricing-card">
          <div className="plan-icon"><BsFillRocketTakeoffFill /></div>
          <div className="plan-name">PLATINUM</div>
          <div className="plan-price">
            12<span>€</span> <small>/ month</small>
          </div>

          <ul className="feature-list">
            <li className="feature-item">
              <span className="feature-dot">•</span>
              <span>7 games per week</span>
            </li>
            <li className="feature-item">
              <FaCheck className="feature-ok" />
              <span>Advanced statistics</span>
            </li>
            <li className="feature-item">
              <FaCheck className="feature-ok" />
              <span>Social gaming</span>
            </li>
          </ul>

          <div className="plan-actions">
            <button className="btn" type="button">CHOOSE</button>
          </div>
        </article>
      </section>
    </main>
  );
}