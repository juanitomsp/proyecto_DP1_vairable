import api from "./api";

const friendsService = {
  getFriends() {
    return api.get("/friendships");
  },

  getPendingRequests() {
    return api.get("/friendships/pending");
  },

  sendFriendRequest(friendId) {
    return api.post(`/friendships/send/${friendId}`);
  },

  acceptFriendRequest(friendshipId) {
    return api.post(`/friendships/${friendshipId}/accept`);
  },

  rejectFriendRequest(friendshipId) {
    return api.delete(`/friendships/${friendshipId}/reject`);
  },
  removeFriendship(friendshipId) {
    return api.delete(`/friendships/${friendshipId}`);
  }
};

export default friendsService;