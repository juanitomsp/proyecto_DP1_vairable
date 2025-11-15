package es.us.dp1.lx_xy_24_25.your_game_name.player;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerRestController {
    
    private final PlayerService playerService;


    @Autowired
    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @GetMapping(value = "{id}")
    public ResponseEntity<Player> findById(Integer id) {
        return new ResponseEntity<>(playerService.findPlayer(id), HttpStatus.OK);
    }
    @GetMapping(value = "{id}/points")
    public ResponseEntity<Integer> getPointsById(@PathVariable Integer id) {
        Optional<Integer> pointsOp = playerService.getPointsById(id);
        return pointsOp.map(p-> ResponseEntity.ok(p))
        .orElse(ResponseEntity.notFound().build());    
    }
    @PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Player> create(@RequestBody @Valid Player player) {
		Player savedPlayer = playerService.savedPlayer(player);
		return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
	}
  

}
