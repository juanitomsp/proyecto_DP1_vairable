package es.us.dp1.lx_xy_24_25.your_game_name.board.dto;

public record MoveResult(boolean ok, String reason, int cost) {
    public static MoveResult ok(int cost){ return new MoveResult(true, null, cost); }
    public static MoveResult fail(String reason){ return new MoveResult(false, reason, 0); }
}