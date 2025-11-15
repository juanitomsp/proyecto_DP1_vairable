package es.us.dp1.lx_xy_24_25.your_game_name.board.dto;

import es.us.dp1.lx_xy_24_25.your_game_name.board.model.TileType;

public record NextTurnView(int currentPlayerIdx, int round, TileType drawn) {}