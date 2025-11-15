package es.us.dp1.lx_xy_24_25.your_game_name.game;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_sessions")
public class GameSession extends BaseEntity {

	@ManyToOne(optional = false)
	@JoinColumn(name = "owner_id")
	private User owner;

	@Min(2)
	@Max(5)
	private int numPlayers;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private GameStatus status = GameStatus.WAITING;

	@Column(unique = true, length = 8, nullable = false)
	private String inviteCode;
	
	@Column(nullable=false)
	private Boolean isPublic;
	
}