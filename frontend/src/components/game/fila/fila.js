
    const getRow = (props, ids) => {

        return (
            <div style={{display:'flex', flexDirection:'row'}}>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(-50px)', backgroundColor:'red'
                }}>
                    <p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{ids[0]}</p>
                </div>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg)', backgroundColor:'blue'
                }}>
                < p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{ids[1]}</p>
                </div>
                <div style={{clipPath: 'polygon(50% 0%,100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)', ...props.style,
                    width:'200px', height:'200px', transform: ' rotate(90deg) translateX(100px) translateY(50px)', backgroundColor:'red'
                }}>
                    < p style={{color:'white', fontSize:'20px', transform: ' rotate(90deg) translateX(100px)'}}>{ids[2]}</p>
                </div>
            </div>
        )
    }




    export const Fila = (props) => {
        const obj = [[1,2,3], [4,5,6], [7,8,9], [10,11,12], [13,14,15], [16,17,18]];
        return (
            <div style={{transform: ' rotate(90deg) translateX(590px)'}}>
                {obj.map((i) => getRow({},i))}
            </div>
        

        )

    }