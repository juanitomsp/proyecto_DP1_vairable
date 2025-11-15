// src/main/java/es/us/dp1/lx_xy_24_25/your_game_name/board/BoardService.java
package es.us.dp1.lx_xy_24_25.your_game_name.board;

import es.us.dp1.lx_xy_24_25.your_game_name.board.dto.NextTurnView;
import es.us.dp1.lx_xy_24_25.your_game_name.board.model.*;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameService;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final BoardRepo boardRepo;
    private final GameService gameService;

    @Autowired
    public BoardService(BoardRepo boardRepo, GameService gameService){
        this.boardRepo = boardRepo;
        this.gameService = gameService;
    }

    @Transactional
    public void initBoard(Integer gameId, int columns){
        GameSession gs = gameService.findById(gameId);

        Board b = new Board();
        b.setPlayersCount(gs.getNumPlayers());
        b.setRound(1);
        b.setCurrentPlayerIdx(0);
        b.setMaxCol(columns - 1);
        b.setEndSpawnsRevealed(false);
        b.setDeck(Deck.standard(System.nanoTime()));

        for (int c = 0; c < columns; c++){
            for (int r = 0; r < 3; r++){
                int id = Board.numberOf(c, r);
                TileType type = (c == 0) ? TileType.SEA : TileType.WATER; 
                Tile t = new Tile(id, type, 0, (byte)0);
                b.getTiles().put(id, t);
            }
        }
        boardRepo.saveForGame(gameId, b);
    }

    @Transactional
    public NextTurnView nextTurn(Integer gameId){
        Board b = boardRepo.findByGameId(gameId).orElseThrow();

        int next = (b.getCurrentPlayerIdx() + 1) % b.getPlayersCount();
        b.setCurrentPlayerIdx(next);

        TileType drawn = b.getDeck() != null ? b.getDeck().draw() : null;

        if ((drawn == null) && !b.isEndSpawnsRevealed()){
            revealEndSpawns(b);
        }

        boardRepo.saveForGame(gameId, b);
        return new NextTurnView(b.getCurrentPlayerIdx(), b.getRound(), drawn);
    }

    private void revealEndSpawns(Board b){
        int lastCol = b.getMaxCol();
        for (int r = 0; r < 3; r++){
            int id = Board.numberOf(lastCol, r);
            if (b.tileExists(id)){
                b.tile(id).setType(TileType.SPAWN);
            }
        }
        b.setEndSpawnsRevealed(true);
    }
}