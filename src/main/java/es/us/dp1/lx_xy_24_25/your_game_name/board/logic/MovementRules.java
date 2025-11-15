package es.us.dp1.lx_xy_24_25.your_game_name.board.logic;

import es.us.dp1.lx_xy_24_25.your_game_name.board.model.*;

import java.util.*;

public final class MovementRules {
    private MovementRules(){}

public static List<Map.Entry<Integer, Direction>> forwardNeighbors(Board b, int fromNumber){
    Tile from = b.tile(fromNumber);
    int c = from.getCol(), r = from.getRow();

    List<Map.Entry<Integer, Direction>> res = new ArrayList<>();

    if (r == 0) {
        if (c + 1 <= b.getMaxCol()) {
            int toE = Board.numberOf(c + 1, 0);
            if (b.tileExists(toE)) res.add(Map.entry(toE, Direction.E));
        }
        int toSE = Board.numberOf(c, 1);
        if (b.tileExists(toSE)) res.add(Map.entry(toSE, Direction.SE));
    } else if (r == 1) {
        if (c + 1 <= b.getMaxCol()) {
            int toNE = Board.numberOf(c + 1, 0);
            if (b.tileExists(toNE)) res.add(Map.entry(toNE, Direction.NE));
            int toE = Board.numberOf(c + 1, 1);
            if (b.tileExists(toE)) res.add(Map.entry(toE, Direction.E));
            int toSE = Board.numberOf(c + 1, 2);
            if (b.tileExists(toSE)) res.add(Map.entry(toSE, Direction.SE));
        }
    } else {
        int toNW = Board.numberOf(c, 1);
        if (b.tileExists(toNW)) res.add(Map.entry(toNW, Direction.NW));
        if (c + 1 <= b.getMaxCol()) {
            int toE = Board.numberOf(c + 1, 2);
            if (b.tileExists(toE)) res.add(Map.entry(toE, Direction.E));
        }
    }
    return res;
}

    public static Optional<Direction> edgeDirection(Board b, int fromNumber, int toNumber){
        for (var e : forwardNeighbors(b, fromNumber)) {
            if (e.getKey() == toNumber) return Optional.of(e.getValue());
        }
        return Optional.empty();
    }

    public static boolean canSwim(Board b, int fromNumber, int toNumber){
        Optional<Direction> dirOpt = edgeDirection(b, fromNumber, toNumber);
        if (dirOpt.isEmpty()) return false;
        Direction d = dirOpt.get();
        Tile from = b.tile(fromNumber);
        Tile to = b.tile(toNumber);
        boolean blocked = from.hasBarrier(d) || to.hasBarrier(d.opposite());
        if (blocked) return false;
        if (b.isFull(to)) return false;
        return true;
    }

    public static Optional<Integer> jumpCost(Board b, int fromNumber, int toNumber){
        Optional<Direction> dirOpt = edgeDirection(b, fromNumber, toNumber);
        if (dirOpt.isPresent()){
            Direction d = dirOpt.get();
            Tile from = b.tile(fromNumber);
            Tile to = b.tile(toNumber);
            boolean blocked = from.hasBarrier(d) || to.hasBarrier(d.opposite());
            if (!blocked) return Optional.empty();
            if (b.isFull(to)) return Optional.empty();
            return Optional.of(1);
        }
        int delta = toNumber - fromNumber;
        if (delta % 3 == 0 && delta >= 6){
            if (!b.tileExists(toNumber)) return Optional.empty();
            if (b.isFull(b.tile(toNumber))) return Optional.empty();
            int steps = delta / 3;
            return Optional.of(1 + (steps - 1));
        }
        return Optional.empty();
    }
}