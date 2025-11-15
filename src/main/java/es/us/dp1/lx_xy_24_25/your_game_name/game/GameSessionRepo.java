package es.us.dp1.lx_xy_24_25.your_game_name.game;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.List;


public interface GameSessionRepo extends CrudRepository<GameSession, Integer> {
	boolean existsByInviteCode(String inviteCode);
	Optional<GameSession> findByInviteCode(String inviteCode);
	List<GameSession> findAll();
	List<GameSession> findByIsPublicTrue();
}
