package es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics;

import java.util.List;

public class StatisticsDTO {

    public record UserSummary(
            Integer userId,
            long gamesPlayed,
            long victories,
            long defeats,
            double avgPoints,
            int maxPoints,
            int minPoints,
            double avgDurationMinutes,
            long totalPlayTimeMinutes
    ) {}

    public record GlobalSummary(
            long gamesRecorded,
            long participations,
            double avgPlayersPerGame,
            double avgPointsPerParticipation,
            int maxPoints,
            int minPoints
    ) {}

    public record RankingEntry(Integer userId, String username, int totalPoints, long victories) {}
    public record Ranking(List<RankingEntry> top) {}
}
