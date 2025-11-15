package es.us.dp1.lx_xy_24_25.your_game_name.board;

import es.us.dp1.lx_xy_24_25.your_game_name.board.dto.NextTurnView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/games/{gameId}/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> init(@PathVariable Integer gameId, @RequestParam int columns){
        boardService.initBoard(gameId, columns);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/turn/next")
    public ResponseEntity<NextTurnView> next(@PathVariable Integer gameId){
        return ResponseEntity.ok(boardService.nextTurn(gameId));
    }
}