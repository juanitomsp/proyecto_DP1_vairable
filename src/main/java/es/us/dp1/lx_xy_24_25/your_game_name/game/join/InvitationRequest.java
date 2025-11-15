package es.us.dp1.lx_xy_24_25.your_game_name.game.join;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {
    private Long receiverId;
    private String gameMode = "default";
}
