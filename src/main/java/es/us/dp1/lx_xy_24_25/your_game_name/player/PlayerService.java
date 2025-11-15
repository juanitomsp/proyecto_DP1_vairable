package es.us.dp1.lx_xy_24_25.your_game_name.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import es.us.dp1.lx_xy_24_25.your_game_name.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;



@Service
public class PlayerService {
    
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player savedPlayer (Player player) throws DataAccessException{
        playerRepository.save(player);
        return player;
    }
    
    @Transactional(readOnly = true)
	public Player findPlayer(Integer id) {
		return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
	}

    @Transactional(readOnly = true)
    public Optional<Integer> getPointsById(Integer id){
        Optional<Player> playerOp = playerRepository.findById(id);
        return playerOp.map(Player::getPoints);
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayerByGameId (Integer gameId){
        return playerRepository.findByGameSessionId(gameId);
    }

}
