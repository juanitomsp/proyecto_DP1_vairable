package es.us.dp1.lx_xy_24_25.your_game_name.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String authority;
}
