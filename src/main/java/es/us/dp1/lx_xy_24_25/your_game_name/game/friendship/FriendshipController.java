package es.us.dp1.lx_xy_24_25.your_game_name.game.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import es.us.dp1.lx_xy_24_25.your_game_name.game.dto.FriendshipView;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }
        
        return userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Usuario actual no encontrado"));
    }

    @PostMapping("/send/{friendId}")
public ResponseEntity<?> sendFriendRequest(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable("friendId") Integer friendId) {
    
    try {
        User user = getAuthenticatedUser(userDetails);
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Friendship friendship = friendshipService.sendFriendRequest(user, friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FriendshipView(friendship));
    } catch (IllegalArgumentException | IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}

    @PostMapping("/{friendshipId}/accept")
public ResponseEntity<?> acceptFriendRequest(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Integer friendshipId) {
    
    try {
        User user = getAuthenticatedUser(userDetails);
        Friendship friendship = friendshipService.acceptFriendRequest(friendshipId, user);
        return ResponseEntity.ok(new FriendshipView(friendship));
    } catch (IllegalArgumentException | IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}
    @GetMapping
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            List<Friendship> friends = friendshipService.getFriends(user);
            
            List<FriendshipView> views = friends.stream()
                .map(FriendshipView::new)
                .toList();
            
            return ResponseEntity.ok(views);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{friendshipId}/reject")
    public ResponseEntity<?> rejectFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer friendshipId) {
        
        try {
            User user = getAuthenticatedUser(userDetails);
            friendshipService.rejectFriendRequest(friendshipId, user);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/pending")
public ResponseEntity<?> getPendingRequests(@AuthenticationPrincipal UserDetails userDetails) {
    try {
        User user = getAuthenticatedUser(userDetails);
        List<Friendship> pending = friendshipService.getPendingRequests(user);
        
        List<FriendshipView> views = pending.stream()
            .map(FriendshipView::new)
            .toList();
        
        return ResponseEntity.ok(views);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}

    @GetMapping("/check/{friendId}")
    public ResponseEntity<?> checkIfFriends(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("friendId") Integer friendId) {
        
        try {
            User currentUser = getAuthenticatedUser(userDetails);
            User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            boolean areFriends = friendshipService.areFriends(currentUser, friend);
            return ResponseEntity.ok(areFriends);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<?> removeFriendship(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer friendshipId) {
        
        try {
            User user = getAuthenticatedUser(userDetails);
            friendshipService.removeFriendship(friendshipId, user);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}
