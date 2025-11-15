package es.us.dp1.lx_xy_24_25.your_game_name.board.dto;

import java.util.List;

public record BoardView(String boardId, int players, int round, int currentPlayerIdx, int maxCol, List<TileView> tiles) {}