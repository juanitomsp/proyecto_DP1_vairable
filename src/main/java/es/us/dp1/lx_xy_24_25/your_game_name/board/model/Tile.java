package es.us.dp1.lx_xy_24_25.your_game_name.board.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tiles")
public class Tile extends BaseEntity {

    @Column(name = "tile_number", nullable = false)
    private int tileNumber;

    @Column(name = "col", nullable = false)
    private int col;

    @Column(name = "row", nullable = false)
    private int row;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 24)
    private TileType type;

    @Column(name = "orientation", nullable = false)
    private int orientation;

    @Column(name = "edge_mask", nullable = false)
    private byte edgeMask;

    @Transient
    private List<Token> tokens = new ArrayList<>();

    public Tile(int tileNumber, TileType type, int orientation, byte edgeMask){
        this.tileNumber = tileNumber;
        this.col = (tileNumber - 1) / 3;
        this.row = (tileNumber - 1) % 3;
        this.type = type;
        this.orientation = ((orientation % 6) + 6) % 6;
        this.edgeMask = (byte)(edgeMask & 0x3F);
    }

    public boolean hasBarrier(Direction d){
        return Direction.hasBarrier(edgeMask, orientation, d);
    }

    public void rotateClockwise(){
        this.orientation = (this.orientation + 1) % 6;
    }

    public int occupants(){
        return tokens.size();
    }

    public int fishSum(){
        return tokens.stream().mapToInt(Token::fishes).sum();
    }

    public int fishSumOf(Integer playerId){
        return tokens.stream().filter(t -> t.playerId().equals(playerId)).mapToInt(Token::fishes).sum();
    }

    public int capacity(int playersCount){
        int base = playersCount;
        return this.type == TileType.ROCK ? Math.max(0, base - 1) : base;
    }

    public boolean isFull(int playersCount){
        return occupants() >= capacity(playersCount);
    }
}