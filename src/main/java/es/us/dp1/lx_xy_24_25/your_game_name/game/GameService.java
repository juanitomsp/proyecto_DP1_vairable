package es.us.dp1.lx_xy_24_25.your_game_name.game;
import java.util.Optional;
import java.util.Random;
import java.util.List;
import java.util.Date;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.your_game_name.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserService;
import es.us.dp1.lx_xy_24_25.your_game_name.player.Player;
import es.us.dp1.lx_xy_24_25.your_game_name.player.PlayerService;

@Service
public class GameService {

	private final GameSessionRepo repo;
	private final UserService userService;
	@Autowired
	public GameService(GameSessionRepo repo, UserService userService, PlayerService playerService) {
		this.repo = repo;
		this.userService = userService;

	}

	@Transactional
	public GameSession findByInviteCode(String code) {
		return repo.findByInviteCode(code).orElseThrow(() -> new ResourceNotFoundException("GameSession", "inviteCode", code));
	}

	@Transactional
	public GameSession create(int numPlayers, boolean isPublic) {
		if (numPlayers < 2 || numPlayers > 5) {
			throw new IllegalArgumentException("numPlayers must be in [2..5]");
		}
		User owner = userService.findCurrentUser();

		GameSession g = new GameSession();
		g.setOwner(owner);
		g.setNumPlayers(numPlayers);
		g.setIsPublic(isPublic);
		g.setInviteCode(generateUniqueCode(6));

		return repo.save(g);
		
	}
	@Transactional
	public List<GameSession> findAll(){
		return (List<GameSession>) repo.findAll();
	}
	@Transactional
	public GameSession findById(Integer id) {
		return repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("GameSession", "id", id));
	}

	@Transactional
	public List<GameSession> findPublicGames(){
		return repo.findByIsPublicTrue();
	}

	@Transactional
	public List<GameSession> findUnfinishedPublicGames(){
		return repo.findByIsPublicTrue().stream().filter(p-> !p.getStatus().equals(GameStatus.FINISHED)).toList();
	}


	private String generateUniqueCode(int len) {
		String code;
		do { code = randomCode(len); }
		while (repo.existsByInviteCode(code));
		return code;
	}

	private String randomCode(int len) {
		final String ALPH = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
		Random r = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) sb.append(ALPH.charAt(r.nextInt(ALPH.length())));
		return sb.toString();
	}


}