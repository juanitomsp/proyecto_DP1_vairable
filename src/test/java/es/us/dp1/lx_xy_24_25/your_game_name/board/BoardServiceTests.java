package es.us.dp1.lx_xy_24_25.your_game_name.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSession;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameService;
import es.us.dp1.lx_xy_24_25.your_game_name.board.model.Board;
import es.us.dp1.lx_xy_24_25.your_game_name.board.model.Tile;
import es.us.dp1.lx_xy_24_25.your_game_name.board.model.TileType;
import es.us.dp1.lx_xy_24_25.your_game_name.board.dto.NextTurnView;

@SpringBootTest
@AutoConfigureTestDatabase
class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @MockBean
    @Autowired
    private BoardRepo boardRepo;

    @MockBean
    @Autowired
    private GameService gameService;

    @Test
    void initBoard_savesBoardWithCorrectProperties() {
        Integer gameId = 77;
        int columns = 4;
        GameSession gs = new GameSession();
        gs.setNumPlayers(3);
        when(gameService.findById(gameId)).thenReturn(gs);

        boardService.initBoard(gameId, columns);

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepo, times(1)).saveForGame(eq(gameId), captor.capture());
        Board saved = captor.getValue();

        assertNotNull(saved);
        assertEquals(gs.getNumPlayers(), saved.getPlayersCount());
        assertEquals(1, saved.getRound());
        assertEquals(0, saved.getCurrentPlayerIdx());
        assertEquals(columns - 1, saved.getMaxCol());
        assertFalse(saved.isEndSpawnsRevealed());
        assertNotNull(saved.getDeck());
        assertEquals(columns * 3, saved.getTiles().size());
        for (int r = 0; r < 3; r++) {
            int id = Board.numberOf(0, r);
            Tile t = saved.getTiles().get(id);
            assertNotNull(t);
            assertEquals(TileType.SEA, t.getType());
        }
        for (int c = 1; c < columns; c++) {
            for (int r = 0; r < 3; r++) {
                int id = Board.numberOf(c, r);
                Tile t = saved.getTiles().get(id);
                assertNotNull(t);
                assertEquals(TileType.WATER, t.getType());
            }
        }
    }

    @Test
    void nextTurn() {
        Integer gameId = 88;
        Board b = new Board();
        b.setPlayersCount(2);
        b.setCurrentPlayerIdx(0);
        b.setRound(1);
        b.setMaxCol(2); 
        b.setEndSpawnsRevealed(false);
        b.setDeck(null);
        for (int c = 0; c <= b.getMaxCol(); c++) {
            for (int r = 0; r < 3; r++) {
                int id = Board.numberOf(c, r);
                Tile t = new Tile(id, TileType.WATER, 0, (byte)0);
                b.getTiles().put(id, t);
            }
        }

        when(boardRepo.findByGameId(gameId)).thenReturn(Optional.of(b));

        NextTurnView view = boardService.nextTurn(gameId);

        assertEquals(1, view.currentPlayerIdx());
        assertNull(view.drawn());
        assertTrue(b.isEndSpawnsRevealed());
        int lastCol = b.getMaxCol();
        for (int r = 0; r < 3; r++) {
            int id = Board.numberOf(lastCol, r);
            Tile t = b.tile(id);
            assertEquals(TileType.SPAWN, t.getType());
        }
        verify(boardRepo, times(1)).saveForGame(eq(gameId), eq(b));
    }
}