package es.us.dp1.lx_xy_24_25.your_game_name.player;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findById(Integer id);

    @Query("SELECT p FROM Player p WHERE p.user.id = :userId")
    Iterable<Player> findAllByUserId(Integer userId);

    List<Player> findByGameSessionId(Integer gameId);
    
}
