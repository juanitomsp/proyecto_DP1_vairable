import React, { useState } from "react";
import UserSummary from "./userSummary";
import GlobalStats from "./globalStats";
import Ranking from "./ranking";
import "../App.css";
import "../static/css/statistics/statistics.css";

export default function StatisticsPage(){
  const [tab, setTab] = useState("user");

  return (
    <main className="page stats">
      <header className="page-header">
        <h1 className="title-xl">STATISTICS</h1>
        <div className="stats__tabs" role="tablist" aria-label="Statistics tabs">
          <button className={`btn ${tab==="user"?"btn--active":""}`} onClick={()=>setTab("user")}  role="tab" aria-selected={tab==="user"}>MY STATS</button>
          <button className={`btn ${tab==="global"?"btn--active":""}`} onClick={()=>setTab("global")} role="tab" aria-selected={tab==="global"}>GLOBAL</button>
          <button className={`btn ${tab==="ranking"?"btn--active":""}`} onClick={()=>setTab("ranking")} role="tab" aria-selected={tab==="ranking"}>RANKING</button>
        </div>
      </header>

      <section className="page-section">
        {tab === "user"    && <UserSummary />}
        {tab === "global"  && <GlobalStats />}
        {tab === "ranking" && <Ranking />}
      </section>
    </main>
  );
}