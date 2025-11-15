package es.us.dp1.lx_xy_24_25.your_game_name.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.us.dp1.lx_xy_24_25.your_game_name.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.your_game_name.player.PlayerService;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserService;

@SpringBootTest
@AutoConfigureTestDatabase
class GameServiceTests {

    @Autowired
    private GameService gameService;

    @Autowired
    @MockBean
    private GameSessionRepo repo;

    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    @MockBean
    private PlayerService playerService;

    @Test
    void create_validParameters_savesGameAndSetsOwnerInviteCode() {
        User owner = new User();
        owner.setId(7);
        owner.setUsername("owner7");

        when(userService.findCurrentUser()).thenReturn(owner);
        when(repo.existsByInviteCode(anyString())).thenReturn(false);
        when(repo.save(any(GameSession.class))).thenAnswer(i -> i.getArgument(0));

        GameSession g = gameService.create(3, true);

        assertNotNull(g);
        assertEquals(owner, g.getOwner());
        assertEquals(3, g.getNumPlayers());
        assertTrue(g.getIsPublic());
        assertNotNull(g.getInviteCode());
        assertEquals(6, g.getInviteCode().length());
        verify(repo, times(1)).save(any(GameSession.class));
    }

    @Test
    void create_invalidNumPlayers() {
        when(userService.findCurrentUser()).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> gameService.create(1, true));
        assertThrows(IllegalArgumentException.class, () -> gameService.create(6, false));
        verify(repo, never()).save(any());
    }

    @Test
    void findByInviteCode_whenFound() {
        GameSession gs = new GameSession();
        gs.setInviteCode("ABC123");
        when(repo.findByInviteCode("ABC123")).thenReturn(Optional.of(gs));

        GameSession r = gameService.findByInviteCode("ABC123");
        assertEquals(gs, r);
    }

    @Test
    void findByInviteCode_whenNotFound() {
        when(repo.findByInviteCode("X")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.findByInviteCode("X"));
    }

    @Test
    void findById_whenFound() {
        GameSession gs = new GameSession();
        gs.setId(5);
        when(repo.findById(5)).thenReturn(Optional.of(gs));

        GameSession r = gameService.findById(5);
        assertEquals(gs, r);
    }

    @Test
    void findById_whenNotFound() {
        when(repo.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.findById(99));
    }

    @Test
    void findAll() {
        GameSession a = new GameSession();
        GameSession b = new GameSession();
        when(repo.findAll()).thenReturn(List.of(a, b));

        var list = gameService.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void findPublicGames() {
        GameSession p = new GameSession();
        p.setIsPublic(true);
        when(repo.findByIsPublicTrue()).thenReturn(List.of(p));

        var list = gameService.findPublicGames();
        assertEquals(1, list.size());
        assertTrue(list.get(0).getIsPublic());
    }
}