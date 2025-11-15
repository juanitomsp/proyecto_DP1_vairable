package es.us.dp1.lx_xy_24_25.your_game_name.game.friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.us.dp1.lx_xy_24_25.your_game_name.user.User;

import java.util.List;
import java.util.Optional;


    
    
@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
    

    @Query("SELECT f FROM Friendship f WHERE f.friend = :user AND f.pending = true")
    List<Friendship> findPendingRequests(@Param("user") User user);
    
    
    @Query("SELECT f FROM Friendship f WHERE (f.user = :user OR f.friend = :user) AND f.pending = false")
    List<Friendship> findAcceptedFriendships(@Param("user") User user);

    
    @Query("SELECT f FROM Friendship f WHERE (f.user = :user AND f.friend = :friend) OR (f.user = :friend AND f.friend = :user)")
    Optional<Friendship> findFriendshipBetween(@Param("user") User user, @Param("friend") User friend);

    
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friendship f WHERE ((f.user = :user AND f.friend = :friend) OR (f.user = :friend AND f.friend = :user)) AND f.pending = false")
    boolean areFriends(@Param("user") User user, @Param("friend") User friend);

    
    List<Friendship> findByUser(User user);
}



