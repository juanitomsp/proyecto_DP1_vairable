package es.us.dp1.lx_xy_24_25.your_game_name.board.model;

public enum Direction {
    W(0), NW(1), NE(2), E(3), SE(4), SW(5);

    public final int bit;
    Direction(int bit){ this.bit = bit; }

    public Direction opposite(){ return values()[(bit + 3) % 6]; }

    public static byte rotate(byte mask, int steps){
        int m = mask & 0x3F;
        int s = ((steps % 6) + 6) % 6;
        int r = ((m << s) | (m >>> (6 - s))) & 0x3F;
        return (byte) r;
    }

    public static byte rotateRight1(byte mask){
        return rotate(mask, 1);
    }

    public static boolean hasBarrier(byte baseMask, int orientation, Direction d){
        int rot = rotate(baseMask, orientation) & 0x3F;
        return ((rot >>> d.bit) & 1) == 1;
    }
}