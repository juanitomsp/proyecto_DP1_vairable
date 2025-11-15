import React, { useEffect, useState } from "react";
import statisticsService from "../services/statisticsService";
import userService from "../services/userService";

export default function UserSummary(){
  const [data, setData] = useState(null);
  const [err,  setErr]  = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    (async () => {
      try{
        const me = await userService.getCurrentUser().then(r => r.data);
        const res = await statisticsService.getUserSummaryByUserId(me.id);
        if (!active) return;
        setData(res.data);
      } catch(e){
        if (!active) return;
        setErr(e.response?.data?.message || e.message);
      } finally {
        if (active) setLoading(false);
      }
    })();
    return () => { active = false; };
  }, []);

  if (loading) return <div className="stats__card">Loading…</div>;
  if (err)     return <div className="stats__card stats__error">{err}</div>;

  return (
    <div className="stats__grid">
      <div className="stats__card"><div className="label">Games played</div><div className="stats__num">{data.gamesPlayed}</div></div>
      <div className="stats__card"><div className="label">Victories</div><div className="stats__num">{data.victories}</div></div>
      <div className="stats__card"><div className="label">Defeats</div><div className="stats__num">{data.defeats}</div></div>
      <div className="stats__card"><div className="label">Avg points</div><div className="stats__num">{data.avgPoints}</div></div>
      <div className="stats__card"><div className="label">Max points</div><div className="stats__num">{data.maxPoints}</div></div>
      <div className="stats__card"><div className="label">Min points</div><div className="stats__num">{data.minPoints}</div></div>
      <div className="stats__card"><div className="label">Avg duration</div><div className="stats__num">{data.avgDurationMinutes} min</div></div>
      <div className="stats__card"><div className="label">Total play time</div><div className="stats__num">{data.totalPlayTimeMinutes} min</div></div>
    </div>
  );
}