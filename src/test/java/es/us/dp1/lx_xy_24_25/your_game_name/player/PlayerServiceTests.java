package es.us.dp1.lx_xy_24_25.your_game_name.player;

import es.us.dp1.lx_xy_24_25.your_game_name.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    void setup() {
        player = new Player();
        player.setId(1);
        player.setPoints(42);
        GameSession game = new GameSession();
        game.setId(10);
        player.setGameSession(game);
    }

    @Test
    void savePlayerAndReturnEntity() throws DataAccessException {
        doAnswer(invocation -> null).when(playerRepository).save(any(Player.class));

        Player saved = playerService.savedPlayer(player);

        assertSame(player, saved);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void throwDataAccessExceptionWhenSaveFails() {
    doThrow(new DataAccessException("...Error de BD...") {})
        .when(playerRepository).save(any(Player.class));

    assertThrows(DataAccessException.class, () -> {
        playerService.savedPlayer(player);
    });

    verify(playerRepository, times(1)).save(player);
}

    @Test
    void findPlayerById() {
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        Player result = playerService.findPlayer(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void throwWhenPlayerNotFound() {
        when(playerRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.findPlayer(999));
        verify(playerRepository, times(1)).findById(999);
    }

    @Test
    void returnPointsWhenPlayerExists() {
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        Optional<Integer> points = playerService.getPointsById(1);

        assertTrue(points.isPresent());
        assertEquals(42, points.get());
        verify(playerRepository).findById(1);
    }


    @Test
    void returnEmptyWhenGettingPointsForMissingPlayer() {
        when(playerRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Integer> points = playerService.getPointsById(2);

        assertFalse(points.isPresent());
        verify(playerRepository).findById(2);
    }

    @Test
    void findPlayersByGameId() {
        when(playerRepository.findByGameSessionId(10)).thenReturn(Arrays.asList(player));

        List<Player> players = playerService.findPlayerByGameId(10);

        assertEquals(1, players.size());
        assertEquals(1, players.get(0).getId());
        verify(playerRepository).findByGameSessionId(10);
    }

    @Test
    void returnEmptyListWhenNoPlayersInGame() {
        when(playerRepository.findByGameSessionId(123)).thenReturn(Arrays.asList());

        List<Player> players = playerService.findPlayerByGameId(123);

        assertNotNull(players);
        assertTrue(players.isEmpty());
        verify(playerRepository).findByGameSessionId(123);
    }
}
