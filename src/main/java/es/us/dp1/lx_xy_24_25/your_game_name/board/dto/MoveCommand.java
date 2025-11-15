package es.us.dp1.lx_xy_24_25.your_game_name.board.dto;

public record MoveCommand(Integer playerId, int fromId, int toId, boolean jump) {}