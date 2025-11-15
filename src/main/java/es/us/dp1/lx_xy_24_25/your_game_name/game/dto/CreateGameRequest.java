package es.us.dp1.lx_xy_24_25.your_game_name.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGameRequest {
	@JsonProperty("isPublic")
	private boolean isPublic;

	@Min(2)
	@Max(5)
	private int numPlayers;
	


}