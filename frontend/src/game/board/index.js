import Hex from "./hex"
import Fila from "./fila"
import Inicio from "./inicio"
import React, { useMemo, useState } from "react";


function visibleBlocks(round) {
  return {
    showInicio: round < 1, 
    showCol1:   round < 2, 
    showCol2:   round < 3, 
    showCol3:   round < 4, 
    showCol4:   round < 5, 
  };
}

function labelsForCol(colIndex) {
  const base = 4 + colIndex * 3;
  return [base, base + 1, base + 2];
}

export const InitBoard= () => {
  const [round, setRound] = useState(0);
  const vis = useMemo(() => visibleBlocks(round), [round]);
  const filaLabels = useMemo(() => [4 + round, 5 + round, 6 + round], [round]);

  
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column", 
        alignItems: "center",
        gap: 12,
        width: "100%",
        minHeight: "100vh",
        boxSizing: "border-box",
        padding: 16,
        background: "#0b1e33",
      }}
    >
      <div
        style={{
          position: "relative",
          width: 1600,     
          height: 900,     
          border: "1px solid #5a7aa6",
          borderRadius: 8,
          overflow: "hidden",
          pointerEvents: "none", 
        }}
      >
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
            position: "absolute",
            inset: 0,
            paddingLeft: 40,
          }}
        >
          {vis.showInicio && <Inicio />}

          <div style={{ marginLeft: "-400px" }}>
            {vis.showCol1 && <Fila />}
          </div>

          <div style={{ marginLeft: "-400px" }}>
            {vis.showCol2 && <Fila labels={filaLabels.map((n) => n + 3)} />}
          </div>

          <div style={{ marginLeft: "-400px" }}>
            {vis.showCol3 && <Fila labels={filaLabels.map((n) => n + 6)} />}
          </div>
        </div>
      </div>
      

      <div style={{ display: "flex", gap: 12, position: "relative", zIndex: 10 }}>
        <div style={{ fontSize: 18, color: "#fff" }}> Ronda: {round}</div>

        <button onClick={() => setRound((r) => Math.max(0, r - 1))}> Ronda - </button>
        <button onClick={() => setRound((r) => r + 1)}> Ronda + </button>
      </div>
    </div>  
  );
}