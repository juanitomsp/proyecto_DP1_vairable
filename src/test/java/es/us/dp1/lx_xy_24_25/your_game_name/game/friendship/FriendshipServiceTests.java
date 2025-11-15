package es.us.dp1.lx_xy_24_25.your_game_name.game.friendship;

import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import es.us.dp1.lx_xy_24_25.your_game_name.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTests {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    private User alice;
    private User bob;

    @BeforeEach
    void setup() {
        alice = new User();
        alice.setId(1);
        alice.setUsername("alice");

        bob = new User();
        bob.setId(2);
        bob.setUsername("bob");
    }

    @Test
    void sendFriendRequest_success_createsPendingFriendship() {
        when(friendshipRepository.findFriendshipBetween(alice, bob)).thenReturn(Optional.empty());
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Friendship result = friendshipService.sendFriendRequest(alice, bob);

        assertNotNull(result);
        assertEquals(alice, result.getUser());
        assertEquals(bob, result.getFriend());
        assertTrue(result.getPending());
        verify(friendshipRepository).findFriendshipBetween(alice, bob);
        verify(friendshipRepository).save(any(Friendship.class));
    }

    @Test
    void sendFriendRequest_fails_whenSendingToSelf() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.sendFriendRequest(alice, alice));
        assertTrue(ex.getMessage().contains("You cannot send a request to yourself"));
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void sendFriendRequest_fails_whenAlreadyFriends() {
        Friendship existing = new Friendship();
        existing.setUser(alice);
        existing.setFriend(bob);
        existing.setPending(false);
        when(friendshipRepository.findFriendshipBetween(alice, bob)).thenReturn(Optional.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> friendshipService.sendFriendRequest(alice, bob));
        assertTrue(ex.getMessage().contains("You are already friends"));
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void sendFriendRequest_fails_whenPendingRequestExists() {
        Friendship existing = new Friendship();
        existing.setUser(alice);
        existing.setFriend(bob);
        existing.setPending(true);
        when(friendshipRepository.findFriendshipBetween(alice, bob)).thenReturn(Optional.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> friendshipService.sendFriendRequest(alice, bob));
        assertTrue(ex.getMessage().contains("pending request"));
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void acceptFriendRequest_success_marksAsNotPending() {
        Friendship pending = new Friendship();
        pending.setId(5);
        pending.setUser(alice);
        pending.setFriend(bob);
        pending.setPending(true);

        when(friendshipRepository.findById(5)).thenReturn(Optional.of(pending));
        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Friendship accepted = friendshipService.acceptFriendRequest(5, bob);

        assertNotNull(accepted);
        assertFalse(accepted.getPending());
        verify(friendshipRepository).findById(5);
        verify(friendshipRepository).save(pending);
    }

    @Test
    void acceptFriendRequest_fails_whenNotReceiver() {
        Friendship pending = new Friendship();
        pending.setId(6);
        pending.setUser(alice);
        pending.setFriend(bob);
        pending.setPending(true);

        when(friendshipRepository.findById(6)).thenReturn(Optional.of(pending));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> friendshipService.acceptFriendRequest(6, alice));
        assertTrue(ex.getMessage().contains("You cannot accept a request"));
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void acceptFriendRequest_fails_whenRequestNotFound() {
        when(friendshipRepository.findById(999)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.acceptFriendRequest(999, bob));
        assertTrue(ex.getMessage().contains("Request not found"));
    }

    @Test
    void rejectFriendRequest_success_deletesEntity() {
        Friendship pending = new Friendship();
        pending.setId(7);
        pending.setUser(alice);
        pending.setFriend(bob);
        pending.setPending(true);

        when(friendshipRepository.findById(7)).thenReturn(Optional.of(pending));
        doNothing().when(friendshipRepository).delete(pending);

        friendshipService.rejectFriendRequest(7, bob);

        verify(friendshipRepository).findById(7);
        verify(friendshipRepository).delete(pending);
    }

    @Test
    void rejectFriendRequest_fails_whenNotReceiver() {
        Friendship pending = new Friendship();
        pending.setId(8);
        pending.setUser(alice);
        pending.setFriend(bob);
        pending.setPending(true);

        when(friendshipRepository.findById(8)).thenReturn(Optional.of(pending));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> friendshipService.rejectFriendRequest(8, alice));
        assertTrue(ex.getMessage().contains("You cannot reject a request"));
        verify(friendshipRepository, never()).delete(any());
    }

    @Test
    void rejectFriendRequest_fails_whenRequestNotFound() {
        when(friendshipRepository.findById(404)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> friendshipService.rejectFriendRequest(404, bob));
        assertTrue(ex.getMessage().contains("Request not found"));
    }

    @Test
    void getFriends_returnsAcceptedFriendships() {
        Friendship f1 = new Friendship();
        f1.setUser(alice);
        f1.setFriend(bob);
        f1.setPending(false);

        when(friendshipRepository.findAcceptedFriendships(alice)).thenReturn(List.of(f1));

        List<Friendship> friends = friendshipService.getFriends(alice);

        assertEquals(1, friends.size());
        assertFalse(friends.get(0).getPending());
        verify(friendshipRepository).findAcceptedFriendships(alice);
    }

    @Test
    void getPendingRequests_returnsIncomingPending() {
        Friendship f1 = new Friendship();
        f1.setUser(alice);
        f1.setFriend(bob);
        f1.setPending(true);

        when(friendshipRepository.findPendingRequests(bob)).thenReturn(List.of(f1));

        List<Friendship> pending = friendshipService.getPendingRequests(bob);

        assertEquals(1, pending.size());
        assertTrue(pending.get(0).getPending());
        verify(friendshipRepository).findPendingRequests(bob);
    }

    @Test
    void getSentRequests_filtersOnlyPending() {
        Friendship fPending = new Friendship();
        fPending.setUser(alice);
        fPending.setFriend(bob);
        fPending.setPending(true);

        Friendship fAccepted = new Friendship();
        fAccepted.setUser(alice);
        fAccepted.setFriend(bob);
        fAccepted.setPending(false);

        when(friendshipRepository.findByUser(alice)).thenReturn(List.of(fPending, fAccepted));

        List<Friendship> sent = friendshipService.getSentRequests(alice);

        assertEquals(1, sent.size());
        assertTrue(sent.get(0).getPending());
        verify(friendshipRepository).findByUser(alice);
    }

    @Test
    void areFriends_delegatesToRepository() {
        when(friendshipRepository.areFriends(alice, bob)).thenReturn(true);

        boolean result = friendshipService.areFriends(alice, bob);

        assertTrue(result);
        verify(friendshipRepository).areFriends(alice, bob);
    }
}
