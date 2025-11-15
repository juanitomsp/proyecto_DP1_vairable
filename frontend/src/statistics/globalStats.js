import React, { useEffect, useState } from "react";
import statisticsService from "../services/statisticsService";

export default function GlobalStats(){
  const [data, setData] = useState(null);
  const [err,  setErr]  = useState(null);

  useEffect(() => {
    statisticsService.getGlobal()
      .then(r => setData(r.data))
      .catch(e => setErr(e.response?.data?.message || e.message));
  }, []);

  if (err)  return <div className="stats__card stats__error">{err}</div>;
  if (!data) return <div className="stats__card">Loading…</div>;

  return (
    <div className="stats__grid">
      <div className="stats__card"><div className="label">Games recorded</div><div className="stats__num">{data.gamesRecorded}</div></div>
      <div className="stats__card"><div className="label">Participations</div><div className="stats__num">{data.participations}</div></div>
      <div className="stats__card"><div className="label">Avg players / game</div><div className="stats__num">{data.avgPlayersPerGame}</div></div>
      <div className="stats__card"><div className="label">Avg points</div><div className="stats__num">{data.avgPointsPerParticipation}</div></div>
      <div className="stats__card"><div className="label">Min points</div><div className="stats__num">{data.minPoints}</div></div>
      <div className="stats__card"><div className="label">Max points</div><div className="stats__num">{data.maxPoints}</div></div>
    </div>
  );
}