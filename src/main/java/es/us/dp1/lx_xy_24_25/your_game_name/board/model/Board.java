package es.us.dp1.lx_xy_24_25.your_game_name.board.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "boards")
public class Board extends BaseEntity {

    @Column(name = "players_count", nullable = false)
    private int playersCount;

    @Column(nullable = false)
    private int round;

    @Column(name = "current_player_idx", nullable = false)
    private int currentPlayerIdx;

    @Column(name = "max_col", nullable = false)
    private int maxCol;

    @Column(name = "end_spawns_revealed", nullable = false)
    private boolean endSpawnsRevealed = false;

    @Transient
    private Deck deck;

    @Transient
    private Map<Integer, Tile> tiles = new HashMap<>();

    public boolean tileExists(int id){
        return tiles.containsKey(id);
    }

    public Tile tile(int id){
        Tile t = tiles.get(id);
        if (t == null) throw new NoSuchElementException("Tile " + id + " not found");
        return t;
    }

    public int capacity(Tile t){
        return t.capacity(playersCount);
    }

    public boolean isFull(Tile t){
        return t.isFull(playersCount);
    }

    public static int numberOf(int col,int row){ return 3*col + row + 1; }
}