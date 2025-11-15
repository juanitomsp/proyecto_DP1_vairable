package es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerGameStatRepository extends CrudRepository<PlayerGameStat, Integer> {
    List<PlayerGameStat> findAll();
    List<PlayerGameStat> findByUser_Id(Integer userId);
    boolean existsByGameId(Integer gameId);
}
