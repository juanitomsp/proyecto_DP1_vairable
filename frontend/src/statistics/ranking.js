import React, { useEffect, useState } from "react";
import statisticsService from "../services/statisticsService";

export default function Ranking(){
  const [rows, setRows] = useState([]);
  const [limit, setLimit] = useState(20);
  const [err, setErr] = useState(null);

  useEffect(() => {
    setErr(null);
    statisticsService.getRankingPoints(limit)
      .then(r => setRows(r.data.top || []))
      .catch(e => setErr(e.response?.data?.message || e.message));
  }, [limit]);

  return (
    <div className="stats__tableWrap">
      <div className="stats__tableHeader">
        <div className="label">TOP by total points</div>
        <div className="stats__limit">
          <span className="label">Show</span>
          <select value={limit} onChange={(e)=>setLimit(Number(e.target.value))}>
            {[10,20,50].map(n => <option key={n} value={n}>{n}</option>)}
          </select>
        </div>
      </div>

      {err && <div className="stats__card stats__error">{err}</div>}

      <table className="stats__table" role="table">
        <thead>
          <tr><th>#</th><th>User</th><th>Total points</th><th>Victories</th></tr>
        </thead>
        <tbody>
          {rows.map((r, i) => (
            <tr key={`${r.userId}-${i}`}>
              <td>{i+1}</td><td>{r.username}</td><td>{r.totalPoints}</td><td>{r.victories}</td>
            </tr>
          ))}
          {rows.length === 0 && !err && <tr><td colSpan="4" className="text-center">No data</td></tr>}
        </tbody>
      </table>
    </div>
  );
}