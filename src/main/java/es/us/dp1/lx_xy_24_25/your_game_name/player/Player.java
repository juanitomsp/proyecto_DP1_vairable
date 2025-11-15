package es.us.dp1.lx_xy_24_25.your_game_name.player;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSession;

@Getter
@Setter
@Entity
@Table (name="players")
public class Player  extends BaseEntity{
    
    @NotNull
    @Column(nullable = false)
    private String color;


    @Column(name = "is_turn", nullable = false)
    private Boolean isTurn;

    
    @Column(nullable = false)
    @Min(0)
    private Integer points = 0;

    @NotNull
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @NotNull
    @ManyToOne(optional=false)
    @JoinColumn(name="game_id", nullable=false)
    private GameSession gameSession;

}
