package es.us.dp1.lx_xy_24_25.your_game_name.game;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.us.dp1.lx_xy_24_25.your_game_name.game.dto.CreateGameRequest;
import es.us.dp1.lx_xy_24_25.your_game_name.game.dto.GameView;
import es.us.dp1.lx_xy_24_25.your_game_name.player.Player;
import es.us.dp1.lx_xy_24_25.your_game_name.player.PlayerService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/games")
@SecurityRequirement(name = "bearerAuth")
public class GameRestController {

	private final GameService gameService;
	private final PlayerService playerService;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public class BadRequestException extends RuntimeException{

	}	

	@Autowired
	public GameRestController(GameService gameService, PlayerService playerService) {
		this.gameService = gameService;
		this.playerService = playerService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<GameView> create(@Valid @RequestBody CreateGameRequest req) {
		return new ResponseEntity<>(GameView.from(gameService.create(req.getNumPlayers(),req.isPublic())), HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<GameView> findById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(GameView.from(gameService.findById(id)), HttpStatus.OK);
	}
	@GetMapping("/invite/{code}")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<GameView> findByInviteCode(@PathVariable("code") String code) {
		if(gameService.findByInviteCode(code)==null){
			throw new BadRequestException();
		}
		GameSession game = gameService.findByInviteCode(code);
		return ResponseEntity.ok(GameView.from(game));
		
	}
	@GetMapping("/{id}/join")
	public ResponseEntity<GameView> joinGame(@PathVariable("id") Integer id) {
		GameSession game = gameService.findById(id);
		return ResponseEntity.ok(GameView.from(game));
	}
	@GetMapping("/{id}/spectate")
	public ResponseEntity<GameView> joinGameAsSpectator(@PathVariable("id") Integer id) {
		GameSession game = gameService.findById(id);
		return ResponseEntity.ok(GameView.from(game));
	}

	@GetMapping
	public ResponseEntity<List<GameView>> findAll() {
		List<GameView> games = gameService.findAll().stream().map(GameView::from).toList();
		return ResponseEntity.ok(games);
	}

	@GetMapping("/public")
	public ResponseEntity<List<GameView>> findPublicGames(){
		List<GameView> games = gameService.findPublicGames().stream().map(GameView::from).toList();
		return ResponseEntity.ok(games);
	}
	@GetMapping("/public/unfinished")
	public ResponseEntity<List<GameView>> findUnfinishedPublicGames(){
		List<GameView> games = gameService.findUnfinishedPublicGames().stream().map(GameView::from).toList();
		return ResponseEntity.ok(games);
	}

	@GetMapping("/{id}/players")
	public ResponseEntity<List<Player>> getPlayersInGame (@PathVariable Integer id){
		List<Player> players = playerService.findPlayerByGameId(id);
        return new ResponseEntity<>(players, HttpStatus.OK);
	}
	

}