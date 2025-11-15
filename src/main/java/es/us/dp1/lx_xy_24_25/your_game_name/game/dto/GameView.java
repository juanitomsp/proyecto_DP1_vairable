package es.us.dp1.lx_xy_24_25.your_game_name.game.dto;

import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSession;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameView {

	private Integer id;
	private Integer ownerId;
	private int numPlayers;
	private GameStatus status;
	private String inviteCode;
	private Boolean isPublic;

	public static GameView from(GameSession g) {
		GameView v = new GameView();
		v.setId(g.getId());
		v.setOwnerId(g.getOwner().getId());
		v.setNumPlayers(g.getNumPlayers());
		v.setStatus(g.getStatus());
		v.setInviteCode(g.getInviteCode());
		v.setIsPublic(g.getIsPublic());
		return v;
	}
}