package es.us.dp1.lx_xy_24_25.your_game_name.board.dto;

import es.us.dp1.lx_xy_24_25.your_game_name.board.model.TileType;
import java.util.List;

public record TileView(
        int id, int col, int row,
        TileType type, int orientation, int edgeMask,
        int occupants, int fishSum,
        List<TileTokenView> tokens
) {}