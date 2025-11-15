package es.us.dp1.lx_xy_24_25.your_game_name.board.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "decks")
public class Deck extends BaseEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "deck_pile", joinColumns = @JoinColumn(name = "deck_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tile_type", nullable = false, length = 32)
    private List<TileType> pile = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "deck_discard", joinColumns = @JoinColumn(name = "deck_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tile_type", nullable = false, length = 32)
    private List<TileType> discard = new ArrayList<>();

    @Column(name = "seed", nullable = false)
    private long seed;

    public static Deck standard(long seed){
        Deck d = new Deck();
        d.seed = seed;
        Map<TileType,Integer> counts = Map.of(
            TileType.BEAR,       3,
            TileType.WATERFALL,  4,
            TileType.WATER,      7,
            TileType.EAGLE,      5,
            TileType.HERON,      5,
            TileType.ROCK,       5
        );
        counts.forEach((t,c) -> { for(int i=0;i<c;i++) d.pile.add(t); });
        Collections.shuffle(d.pile, new Random(seed));
        return d;
    }

    public boolean isEmpty(){ return pile.isEmpty(); }

    public TileType draw(){
        if (pile.isEmpty()) return null;
        TileType c = pile.remove(pile.size() - 1);
        discard.add(c);
        return c;
    }
}