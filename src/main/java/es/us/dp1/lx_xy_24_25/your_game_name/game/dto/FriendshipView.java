package es.us.dp1.lx_xy_24_25.your_game_name.game.dto;
import es.us.dp1.lx_xy_24_25.your_game_name.game.friendship.Friendship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;

@Getter
@Setter
@AllArgsConstructor
public class FriendshipView {
    private Integer id;
    private UserView user;
    private UserView friend;
    private Boolean pending;

    public FriendshipView(Friendship friendship) {
        this.id = friendship.getId();
        this.user = new UserView(friendship.getUser());
        this.friend = new UserView(friendship.getFriend());
        this.pending = friendship.getPending();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserView {
        private Integer id;
        private String username;

        public UserView(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }
}

