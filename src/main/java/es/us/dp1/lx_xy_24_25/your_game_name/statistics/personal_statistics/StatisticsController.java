package es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @GetMapping("/global")
    public ResponseEntity<StatisticsDTO.GlobalSummary> global(){
        return ResponseEntity.ok(service.getGlobalSummary());
    }

    @GetMapping("/users/{userId}/summary")
    public ResponseEntity<StatisticsDTO.UserSummary> user(@PathVariable Integer userId){
        return ResponseEntity.ok(service.getUserSummary(userId));
    }

    @GetMapping("/ranking/points")
    public ResponseEntity<StatisticsDTO.Ranking> ranking(@RequestParam(defaultValue = "20") int limit){
        return ResponseEntity.ok(service.getRankingByTotalPoints(Math.max(1, limit)));
    }

    @PostMapping("/games/{gameId}/record")
    public ResponseEntity<Void> record(
            @PathVariable Integer gameId,
            @RequestParam Integer winnerUserId,
            @RequestParam(defaultValue = "0") int durationSeconds,
            @RequestBody Map<Integer, Integer> userPoints 
    ){
        service.recordGameResult(gameId, userPoints, winnerUserId, durationSeconds, Instant.now());
        return ResponseEntity.ok().build();
    }
}
