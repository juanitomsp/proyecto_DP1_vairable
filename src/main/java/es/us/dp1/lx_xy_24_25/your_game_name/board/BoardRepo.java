package es.us.dp1.lx_xy_24_25.your_game_name.board;

import es.us.dp1.lx_xy_24_25.your_game_name.board.model.Board;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BoardRepo {
    private final Map<Integer, Board> byGameId = new ConcurrentHashMap<>();

    public Optional<Board> findByGameId(Integer gameId){
        return Optional.ofNullable(byGameId.get(gameId));
    }

    public Board saveForGame(Integer gameId, Board board){
        byGameId.put(gameId, board);
        return board;
    }

    public void delete(Integer gameId){
        byGameId.remove(gameId);
    }
}