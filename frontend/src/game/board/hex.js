
const colorByType = (type) => {
  switch (type) {
    case "SEA": return "#71a2f2ff";
    case "SPAWN": return "#241dd5ff";
    case "WATER": return "#c8f7ff";
    case "BEAR": return "#5a3131ff";
    case "EAGLE": return "#f7c95a";
    case "HERON": return "#57c457";
    case "ROCK": return "#e49021ff";
    case "WATERFALL": return "#9cd1ff";
    default: return "#110f0fff";
  }
};

const MID = [
  { x: 97, y: 50 },   
  { x: 77, y: 15 },  
  { x: 23, y: 15 },   
  { x: 3,  y: 50 },   
  { x: 23, y: 85 },   
  { x: 77, y: 85 },   
];

export default function Hex({
  label = "",
  type = "WATER",
  size = 200,
  translate = { x: 0, y: 0 },
  rotation = 1,                
  edges = [true, false, false, false, false, false],
  style = {},
  baseRotate = 0,             
}) {
  const { x, y } = translate;
  const totalRotateDeg = baseRotate + rotation * 60;
  const dot = Math.max(8, size * 0.12);

  return (
    
    <div
      style={{
        position: "absolute",
        width: size,
        height: size,
        transform: `translate(${x}px, ${y}px)`,
        ...style,
      }}
    >
      <div
        style={{
          width: "100%",
          height: "100%",
          backgroundColor: colorByType(type),
          clipPath: "polygon(50% 0%,100% 25%,100% 75%,50% 100%,0% 75%,0% 25%)",
          boxShadow: "0 2px 6px rgba(0,0,0,0.15)",
        }}
      />

      <div style={{ position: "absolute", inset: 0, transform: `rotate(${totalRotateDeg}deg)` }}>
        {edges.map((on, i) =>
          on ? (
            <div
              key={i}
              style={{
                position: "absolute",
                left: `${MID[i].x}%`,
                top:  `${MID[i].y}%`,
                width: dot * 1.8,
                height: dot * 0.6,
                background: "#111",
                borderRadius: dot,
                transform: "translate(-50%, -50%)",
                opacity: 0.9,
                pointerEvents: "none",
              }}
            />
          ) : null
        )}
      </div>

      <div
        style={{
          position: "absolute",
          inset: 0,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "#fff",
          fontSize: 20,
          fontWeight: 700,
          userSelect: "none",
        }}
      >
        {label}
      </div>
    </div>
  );
}



    
    
    
    /* const getHex = (props, ids) => {

        return (
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(180deg) translateX(-100px) translateY(-50px)', backgroundColor:'red'
                }}>
                    <p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{ids[0]}</p>
                </div>
        )
    }

    export const Hex = (props) => {
        return (
            <div>
                {getHex({}, [1])}
            </div>
        )
    }
*/