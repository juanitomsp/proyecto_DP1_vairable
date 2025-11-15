

import Hex from "./hex";

const GAP_X = -100; // separación horizontal entre las 3
const GAP_Y = 150; // separación vertical arriba/abajo

export default function Inicio({
  base = { x: 590, y: 200 }, // ancla donde colocas esta fila
  size = 200,
  colors = ["blue", "red", "green", "yellow"],
  labels = [0, 1, 2, 3],
}) {
  const { x, y } = base;

  return (
    <div
      style={{
        position: "relative",
        width: `${size * 3}px`,
        height: `${size * 2}px`,
        transform: `rotate(0deg) translate(0px, 0px)`,
      }}
    >
      <Hex
        size={size}
        color={colors[0]}
        label={labels[0]}
        translate={{ x: x + 2*GAP_X, y: y + 0 }}
      />
      <Hex
        size={size}
        color={colors[1]}
        label={labels[1]}
        translate={{ x: x + GAP_X, y: y - GAP_Y }}
      />

      <Hex
        size={size}
        color={colors[2]}
        label={labels[2]}
        translate={{ x: x + 0, y: y + 0 }}
      />
      

      <Hex
        size={size}
        color={colors[3]}
        label={labels[3]}
        translate={{ x: x + GAP_X, y: y + GAP_Y}}
      />
      
    </div>
  );
}




/* const getInicio = (props) => {

        return (
            <div style={{display:'flex', flexDirection:'row'}}>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(-50px)', backgroundColor:'blue'
                }}>
                    <p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{1}</p>
                </div>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg)', backgroundColor:'blue'
                }}>
                < p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{2}</p>
                </div>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(50px)', backgroundColor:'blue'
                }}>
                    < p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{3}</p>
                </div>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' translateX(-400px) translateY(200px) rotate(90deg)', backgroundColor:'blue'
                }}>
                < p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{0}</p>
                </div>
            </div>
        )
    }

    export const Inicio = (props) => {
        return (
            <div style={{transform: ' rotate(90deg) translateX(700px) translateY(400px'}}>
                {getInicio({})}
            </div> 
        )

    }*/