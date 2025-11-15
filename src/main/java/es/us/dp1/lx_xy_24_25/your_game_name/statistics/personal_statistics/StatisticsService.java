// src/main/java/es/us/dp1/lx_xy_24_25/your_game_name/statistics/personal_statistics/StatisticsService.java
package es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserService;

@Service
public class StatisticsService {

    private final PlayerGameStatRepository statRepo;
    private final UserService userService;

    public StatisticsService(PlayerGameStatRepository statRepo, UserService userService) {
        this.statRepo = statRepo;
        this.userService = userService;
    }

    @Transactional
    public void recordGameResult(
            Integer gameId,
            Map<Integer, Integer> userPoints,
            Integer winnerUserId,
            int durationSeconds,
            Instant finishedAt
    ){
        if (statRepo.existsByGameId(gameId)) return; 
        for (Map.Entry<Integer,Integer> e : userPoints.entrySet()){
            User u = userService.findUser(e.getKey());
            boolean win = Objects.equals(e.getKey(), winnerUserId);
            PlayerGameStat s = new PlayerGameStat(u, gameId, Math.max(0, e.getValue()), win,
                    Math.max(0, durationSeconds), finishedAt);
            statRepo.save(s);
        }
    }

    @Transactional(readOnly = true)
    public StatisticsDTO.UserSummary getUserSummary(Integer userId){
        var rows = statRepo.findByUser_Id(userId);
        long played = rows.size();
        if (played == 0){
            return new StatisticsDTO.UserSummary(userId, 0,0,0,0,0,0,0,0);
        }
        long wins = rows.stream().filter(PlayerGameStat::getIsWinner).count();
        long defeats = played - wins;

        var pts = rows.stream().mapToInt(PlayerGameStat::getPoints).summaryStatistics();
        double avgPts = round2(pts.getAverage());
        int maxPts = pts.getMax();
        int minPts = pts.getMin();

        var dur = rows.stream().mapToInt(PlayerGameStat::getDurationSeconds).summaryStatistics();
        double avgDurMin = round2(dur.getAverage() / 60.0);
        long totalMin = dur.getSum() / 60;

        return new StatisticsDTO.UserSummary(userId, played, wins, defeats, avgPts, maxPts, minPts, avgDurMin, totalMin);
    }

    @Transactional(readOnly = true)
    public StatisticsDTO.GlobalSummary getGlobalSummary(){
        var all = statRepo.findAll();
        long participations = all.size();
        if (participations == 0){
            return new StatisticsDTO.GlobalSummary(0, 0, 0, 0, 0, 0);
        }
        long gamesRecorded = all.stream().map(PlayerGameStat::getGameId).distinct().count();

        Map<Integer, Long> byGame = all.stream()
                .collect(Collectors.groupingBy(PlayerGameStat::getGameId, Collectors.counting()));
        double avgPlayersPerGame = round2(byGame.values().stream().mapToLong(Long::longValue).average().orElse(0));

        var pts = all.stream().mapToInt(PlayerGameStat::getPoints).summaryStatistics();
        double avgPoints = round2(pts.getAverage());
        int maxPts = pts.getMax();
        int minPts = pts.getMin();

        return new StatisticsDTO.GlobalSummary(gamesRecorded, participations, avgPlayersPerGame, avgPoints, maxPts, minPts);
    }

    @Transactional(readOnly = true)
    public StatisticsDTO.Ranking getRankingByTotalPoints(int limit){
        var all = statRepo.findAll();

        Map<Integer, Integer> totalByUser = all.stream()
                .collect(Collectors.groupingBy(s -> s.getUser().getId(),
                        Collectors.summingInt(PlayerGameStat::getPoints)));

        Map<Integer, Long> winsByUser = all.stream()
                .collect(Collectors.groupingBy(s -> s.getUser().getId(),
                        Collectors.filtering(PlayerGameStat::getIsWinner, Collectors.counting())));

        List<StatisticsDTO.RankingEntry> top =
        totalByUser.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map((Map.Entry<Integer, Integer> e) -> {
                    User u = userService.findUser(e.getKey());
                    long wins = winsByUser.getOrDefault(e.getKey(), 0L);
                    return new StatisticsDTO.RankingEntry(
                            u.getId(), u.getUsername(), e.getValue(), wins
                    );
                })
                .collect(Collectors.toList());

        return new StatisticsDTO.Ranking(top);
    }

    private double round2(double v){ return Math.round(v * 100.0) / 100.0; }
}
