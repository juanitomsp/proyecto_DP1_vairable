package es.us.dp1.lx_xy_24_25.your_game_name.statistics.personal_statistics;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "player_game_stats")
public class PlayerGameStat extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Min(0)
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "is_winner", nullable = false)
    private Boolean isWinner = false;

    @Min(0)
    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds = 0;

    @Column(name = "finished_at", nullable = false)
    private Instant finishedAt = Instant.now();

    public PlayerGameStat(User user, Integer gameId, Integer points, boolean isWinner, int durationSeconds, Instant finishedAt){
        this.user = user;
        this.gameId = gameId;
        this.points = points;
        this.isWinner = isWinner;
        this.durationSeconds = durationSeconds;
        this.finishedAt = finishedAt == null ? Instant.now() : finishedAt;
    }
}
