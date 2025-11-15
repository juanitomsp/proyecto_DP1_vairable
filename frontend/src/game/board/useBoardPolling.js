// frontend/src/game/board/useBoardPolling.js
import { useEffect, useRef, useState } from 'react';
import { getBoardView } from '../../api/board';

export default function useBoardPolling(gameId, ms=1000){
  const [board, setBoard] = useState(null);
  const [error, setError] = useState(null);
  const inflight = useRef(false);
  async function tick(){
    if (inflight.current || document.hidden) return;
    inflight.current = true;
    try { setBoard(await getBoardView(gameId)); setError(null); }
    catch(e){ setError(e); }
    finally { inflight.current = false; }
  }
  useEffect(() => {
    tick();
    const id = setInterval(tick, ms);
    const onVis = () => { if (!document.hidden) tick(); };
    document.addEventListener('visibilitychange', onVis);
    return () => { clearInterval(id); document.removeEventListener('visibilitychange', onVis); };
  }, [gameId, ms]);
  return { board, error, reload: tick };
}
