package es.us.dp1.lx_xy_24_25.your_game_name.game.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserRepository;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserService;
import es.us.dp1.lx_xy_24_25.your_game_name.game.GameSessionRepo;
import es.us.dp1.lx_xy_24_25.your_game_name.game.friendship.Friendship;
import es.us.dp1.lx_xy_24_25.your_game_name.game.friendship.FriendshipRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

   private final FriendshipRepository friendshipRepository;
   private final UserRepository userRepository;

    @Autowired
	public FriendshipService(FriendshipRepository friendshipRepo, UserRepository userRepository) {
		this.friendshipRepository = friendshipRepo;
		this.userRepository = userRepository;
	}

    @Transactional
    public Friendship sendFriendRequest(User user, User friend) {
        if (user.equals(friend)) {
            throw new IllegalArgumentException("You cannot send a request to yourself.");
        }

        Optional<Friendship> existing = friendshipRepository.findFriendshipBetween(user, friend);
        if (existing.isPresent()) {
            Friendship friendship = existing.get();
            if (!friendship.getPending()) {
                throw new IllegalStateException("You are already friends.");
            } else {
                throw new IllegalStateException("There is already a pending request between these users.");
            }
        }

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setPending(true);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship acceptFriendRequest(Integer friendshipId, User user) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));

        if (!friendship.getFriend().equals(user)) {
            throw new IllegalStateException("You cannot accept a request that is not addressed to you.");
        }

        friendship.setPending(false);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Integer friendshipId, User user) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));

        
        if (!friendship.getFriend().equals(user)) {
            throw new IllegalStateException("You cannot reject a request that is not addressed to you.");
        }

        friendshipRepository.delete(friendship);
    }

     @Transactional(readOnly = true)
    public List<Friendship> getFriends(User user) {
        return friendshipRepository.findAcceptedFriendships(user);
    }

    
    @Transactional(readOnly = true)
    public List<Friendship> getPendingRequests(User user) {
        return friendshipRepository.findPendingRequests(user);
    }


    
    @Transactional(readOnly = true)
    public List<Friendship> getSentRequests(User user) {
        return friendshipRepository.findByUser(user)
                .stream()
                .filter(Friendship::getPending)
                .toList();
    }

    
    @Transactional(readOnly = true)
    public boolean areFriends(User user, User friend) {
        return friendshipRepository.areFriends(user, friend);
    }

    @Transactional
    public void removeFriendship(Integer friendshipId,User user) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found."));
        
        friendshipRepository.delete(friendship);
    }

}
