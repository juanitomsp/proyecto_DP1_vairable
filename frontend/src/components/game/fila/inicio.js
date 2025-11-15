

export const Inicio = (props) => {

    return (
        <div style={{display:'flex', flexDirection:'row'}}>
            <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(-50px)', backgroundColor:'red'
            }}>
            </div>
            <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                width:'200px', height:'200px', transform: ' rotate(90deg)', backgroundColor:'blue'
            }}>
            </div>
            <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(50px)', backgroundColor:'red'
            }}>
            </div>
            <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                width:'200px', height:'200px', transform: ' rotate(90deg)  translateX(100px)', backgroundColor:'blue'
            }}>
            </div>
        </div>

    )

}