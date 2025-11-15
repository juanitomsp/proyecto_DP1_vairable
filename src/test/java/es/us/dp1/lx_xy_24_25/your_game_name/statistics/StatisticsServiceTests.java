package es.us.dp1.lx_xy_24_25.your_game_name.statistics;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics.PlayerGameStat;
import es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics.PlayerGameStatRepository;
import es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics.StatisticsDTO;
import es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics.StatisticsService;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserService;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Users & Admin Module")
@Feature("Statistics Management")
@SpringBootTest
@AutoConfigureTestDatabase
class StatisticsServiceTests {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    @MockBean
    private PlayerGameStatRepository statRepo;

    @Autowired
    @MockBean
    private UserService userService;

    @Test
    void recordGameResult_whenExists() {
        Integer gameId = 42;
        when(statRepo.existsByGameId(gameId)).thenReturn(true);

        Map<Integer, Integer> points = Map.of(1, 10, 2, 5);
        statisticsService.recordGameResult(gameId, points, 1, 120, Instant.now());

        verify(statRepo, never()).save(any());
    }

    @Test
    void recordGameResult_savesOneEntryPerPlayer() {
        Integer gameId = 100;
        when(statRepo.existsByGameId(gameId)).thenReturn(false);

        User u1 = new User(); u1.setId(1); u1.setUsername("u1");
        User u2 = new User(); u2.setId(2); u2.setUsername("u2");
        when(userService.findUser(1)).thenReturn(u1);
        when(userService.findUser(2)).thenReturn(u2);

        Map<Integer, Integer> points = new LinkedHashMap<>();
        points.put(1, 15);
        points.put(2, 7);

        Instant now = Instant.now();
        statisticsService.recordGameResult(gameId, points, 1, 90, now);

        ArgumentCaptor<PlayerGameStat> captor = ArgumentCaptor.forClass(PlayerGameStat.class);
        verify(statRepo, times(2)).save(captor.capture());

        List<PlayerGameStat> saved = captor.getAllValues();
        
        boolean foundWinner = saved.stream().anyMatch(s -> s.getUser().getId().equals(1) && s.getIsWinner());
        boolean foundLoser = saved.stream().anyMatch(s -> s.getUser().getId().equals(2) && !s.getIsWinner());
        assertTrue(foundWinner);
        assertTrue(foundLoser);
        saved.forEach(s -> {
            assertTrue(s.getPoints() >= 0);
            assertEquals(90, s.getDurationSeconds());
            assertEquals(gameId, s.getGameId());
            assertEquals(now, s.getFinishedAt());
        });
    }

    @Test
    void getUserSummary_returnsZeros() {
        Integer userId = 10;
        when(statRepo.findByUser_Id(userId)).thenReturn(Collections.emptyList());

        StatisticsDTO.UserSummary summary = statisticsService.getUserSummary(userId);
        assertEquals(userId, summary.userId());
        assertEquals(0, summary.gamesPlayed());
        assertEquals(0, summary.victories());
        assertEquals(0, summary.defeats());
        assertEquals(0.0, summary.avgPoints());
        assertEquals(0, summary.maxPoints());
        assertEquals(0, summary.minPoints());
    }

    @Test
    void getUserSummary_computesCorrectMetrics() {
        Integer userId = 5;
        User u = new User(); u.setId(userId); u.setUsername("player5");

        PlayerGameStat s1 = new PlayerGameStat(u, 1, 10, true, 120, Instant.now());
        PlayerGameStat s2 = new PlayerGameStat(u, 2, 4, false, 180, Instant.now());

        when(statRepo.findByUser_Id(userId)).thenReturn(List.of(s1, s2));

        StatisticsDTO.UserSummary summary = statisticsService.getUserSummary(userId);
        assertEquals(2, summary.gamesPlayed());
        assertEquals(1, summary.victories());
        assertEquals(1, summary.defeats());
        assertEquals(7.0, summary.avgPoints()); 
        assertEquals(10, summary.maxPoints());
        assertEquals(4, summary.minPoints());
        
        assertEquals(2.5, summary.avgDurationMinutes());
        assertEquals((120 + 180) / 60, summary.totalPlayTimeMinutes());
    }

    @Test
    void getGlobalSummary_returnsZeros() {
        when(statRepo.findAll()).thenReturn(Collections.emptyList());

        StatisticsDTO.GlobalSummary g = statisticsService.getGlobalSummary();
        assertEquals(0, g.gamesRecorded());
        assertEquals(0, g.participations());
        assertEquals(0.0, g.avgPlayersPerGame());
        assertEquals(0.0, g.avgPointsPerParticipation());
    }

    @Test
    void getGlobalSummary_computesAggregates() {
        User u1 = new User(); u1.setId(1); u1.setUsername("a");
        User u2 = new User(); u2.setId(2); u2.setUsername("b");

        PlayerGameStat g1p1 = new PlayerGameStat(u1, 1, 5, false, 100, Instant.now());
        PlayerGameStat g1p2 = new PlayerGameStat(u2, 1, 12, true, 120, Instant.now());
        PlayerGameStat g2p1 = new PlayerGameStat(u1, 2, 8, true, 80, Instant.now());

        when(statRepo.findAll()).thenReturn(List.of(g1p1, g1p2, g2p1));

        StatisticsDTO.GlobalSummary g = statisticsService.getGlobalSummary();
        assertEquals(2, g.gamesRecorded()); 
        assertEquals(3, g.participations());
        assertEquals(1.5, g.avgPlayersPerGame());
        assertEquals(8.33, g.avgPointsPerParticipation());
        assertEquals(5, g.minPoints());
        assertEquals(12, g.maxPoints());
    }

    @Test
    void getRankingByTotalPoints() {
        User u1 = new User(); u1.setId(1); u1.setUsername("user1");
        User u2 = new User(); u2.setId(2); u2.setUsername("user2");

        PlayerGameStat a1 = new PlayerGameStat(u1, 1, 10, true, 100, Instant.now());
        PlayerGameStat a2 = new PlayerGameStat(u1, 2, 10, true, 90, Instant.now());
        PlayerGameStat b1 = new PlayerGameStat(u2, 3, 10, true, 80, Instant.now());

        when(statRepo.findAll()).thenReturn(List.of(a1, a2, b1));
        when(userService.findUser(1)).thenReturn(u1);
        when(userService.findUser(2)).thenReturn(u2);

        StatisticsDTO.Ranking ranking = statisticsService.getRankingByTotalPoints(10);
        assertNotNull(ranking);
        List<StatisticsDTO.RankingEntry> entries = ranking.top();
        assertEquals(2, entries.size());
        assertEquals(1, entries.get(0).userId());
        assertEquals(20, entries.get(0).totalPoints());
        assertEquals(2L, entries.get(0).victories());
        assertEquals(2, entries.get(1).userId());
        assertEquals(10, entries.get(1).totalPoints());
        assertEquals(1L, entries.get(1).victories());
    }
}