import React, { useEffect, useState, useCallback } from "react";
import tokenService from "../services/token.service";
import friendsService from "../services/friendsService";
import "../static/css/friends/friends.css";
import "../static/css/game/join/join.css";


export default function Friends() {
  const currentUser = tokenService.getUser();
  const currentUserId = currentUser?.id;

  const [friends, setFriends] = useState([]);
  const [pendingRequests, setPendingRequests] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [friendSearch, setFriendSearch] = useState("");
  const [userSearch, setUserSearch] = useState("");
  const [busyId, setBusyId] = useState(null);

  const fetchAllUsers = useCallback(async () => {
    try {
      const jwt = tokenService.getLocalAccessToken();
      const resp = await fetch("/api/v1/users", {
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const json = await resp.json();
      if (!resp.ok) throw new Error("Failed to load users");
      setAllUsers(Array.isArray(json) ? json : []);
    } catch (err) {
      console.error(err);
      setMessage("Could not load users for search.");
    }
  }, []);

  const fetchFriendsData = useCallback(async () => {
    try {
      const [friendsResp, pendingResp] = await Promise.all([
        friendsService.getFriends(),
        friendsService.getPendingRequests(),
      ]);
      setFriends(Array.isArray(friendsResp.data) ? friendsResp.data : []);
      setPendingRequests(Array.isArray(pendingResp.data) ? pendingResp.data : []);
    } catch (err) {
      console.error("Error in fetchFriendsData:", err);
      const errorMsg = err.response?.data?.message || err.message || "Failed to load data";
      setMessage(errorMsg);
    }
  }, []);

  useEffect(() => {
    fetchFriendsData();
    fetchAllUsers();
  }, [fetchFriendsData, fetchAllUsers]);

  async function handleSendRequest(e) {
    e.preventDefault();
    if (!userSearch.trim()) return;
    setLoading(true);
    setMessage(null);
    try {
      const user = allUsers.find(
        (u) => u.username.toLowerCase() === userSearch.toLowerCase()
      );
      if (!user) throw new Error("User not found");
      await friendsService.sendFriendRequest(user.id);
      setMessage("Friend request sent!");
      setUserSearch("");
      await fetchFriendsData();
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || "Failed to send request";
      setMessage(errorMsg);
    } finally {
      setLoading(false);
    }
  }

  async function handleAccept(friendshipId) {
    setBusyId(`accept-${friendshipId}`);
    setMessage(null);
    try {
      await friendsService.acceptFriendRequest(friendshipId);
      setMessage("Friend request accepted!");
      await fetchFriendsData();
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || "Failed to accept request";
      setMessage(errorMsg);
    } finally {
      setBusyId(null);
    }
  }

  async function handleReject(friendshipId) {
    setBusyId(`reject-${friendshipId}`);
    setMessage(null);
    try {
      await friendsService.rejectFriendRequest(friendshipId);
      setMessage("Friend request rejected!");
      await fetchFriendsData();
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || "Failed to reject request";
      setMessage(errorMsg);
    } finally {
      setBusyId(null);
    }
  }

  async function handleRemoveFriend(friendshipId) {
    setBusyId(`remove-${friendshipId}`);
    setMessage(null);
    try {
      await friendsService.removeFriendship(friendshipId);
      setMessage("Friendship removed successfully!");
      await fetchFriendsData();
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || "Failed to remove friendship";
      setMessage(errorMsg);
    } finally {
      setBusyId(null);
    }
  }

  const getFriendUser = (friendship) =>
    friendship.user?.id === currentUserId ? friendship.friend : friendship.user;

  const filteredFriends = friends.filter((f) => {
    const friendUser = getFriendUser(f);
    return friendUser?.username?.toLowerCase().includes(friendSearch.toLowerCase());
  });

  const successMsg = message && /(success|accepted|sent)/i.test(message);

  return (
      <div className="friends-page-container"> 
      <section className="create__hero">
      <h1 className="title-xl" style = {{fontSize: 150}}> FRIENDS </h1>
        <div className="friends-form-container friends__wrap">
          <div className="friends__columns">
            <div className="join__public">
            <div className="friends__subtitle">MY FRIENDS</div>
            <div className="form-field" style={{ marginTop: 8, marginBottom: 12 }}>
              <label className="label" htmlFor="friendSearch">SEARCH FRIENDS</label>
              <input
                type="text"
                id="friendSearch"
                className="input"
                placeholder="Search by username..."
                value={friendSearch}
                onChange={(e) => setFriendSearch(e.target.value)}
              />
            </div>

            <div className="friends__table-wrap">
              <table className="friends__table" aria-label="friends list">
                <thead style={{justifyContent: "center"}}>
                  <tr style={{justifyContent: "center"}}>
                    <th>Username</th>
                    <th className="friends__th-actions">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredFriends.length === 0 ? (
                    <tr>
                      <td colSpan="2" className="friends__empty">No friends found.</td>
                    </tr>
                  ) : (
                    filteredFriends.map((f) => {
                      const friendUser = getFriendUser(f);
                      return (
                        <tr key={f.id}>
                          <td className="friends__user">{friendUser?.username || "Unknown"}</td>
                          <td className="friends__actions" style={{justifyContent: "center"}}>
                            <button
                              className="btn btn-danger btn-slim"
                              onClick={() => handleRemoveFriend(f.id)}
                              disabled={busyId === `remove-${f.id}`}
                              aria-label={`Remove ${friendUser?.username}`}
                            >
                              {busyId === `remove-${f.id}` ? "..." : "REMOVE"}
                            </button>
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
          </div>

            <div className="join__private">
            <div className="friends__subtitle">ADD FRIEND</div>

            <form onSubmit={handleSendRequest} className="friends__form">
              <div className="form-field" style={{ marginTop: 8 }}>
                <label className="label" htmlFor="userSearch">ENTER USERNAME</label>
                <input
                  type="text"
                  id="userSearch"
                  className="input"
                  placeholder="e.g. john_doe"
                  value={userSearch}
                  onChange={(e) => setUserSearch(e.target.value)}
                  required
                />
              </div>

              <button
                className="btn btn-lg"
                type="submit"
                disabled={loading || userSearch.trim().length < 1}
                style={{ marginTop: 12 }}
              >
                {loading ? "SENDING..." : "SEND REQUEST"}
              </button>
            </form>

            <div className="friends__subtitle" style={{ marginTop: 24 }}>
              PENDING REQUESTS
            </div>

            <div className="friends__table-wrap">
              <table className="friends__table" aria-label="pending requests">
                <thead>
                  <tr>
                    <th>From</th>
                    <th className="friends__th-actions">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {pendingRequests.length === 0 ? (
                    <tr>
                      <td colSpan="2" className="friends__empty">No pending requests.</td>
                    </tr>
                  ) : (
                    pendingRequests.map((req) => (
                      <tr key={req.id}>
                        <td className="friends__user">{req.user?.username || "Unknown"}</td>
                        <td className="friends__actions">
                          <button
                            className="btn btn-slim"
                            onClick={() => handleAccept(req.id)}
                            disabled={busyId === `accept-${req.id}`}
                          >
                            {busyId === `accept-${req.id}` ? "..." : "ACCEPT"}
                          </button>
                          <button
                            className="btn btn-danger btn-slim"
                            onClick={() => handleReject(req.id)}
                            disabled={busyId === `reject-${req.id}`}
                          >
                            {busyId === `reject-${req.id}` ? "..." : "REJECT"}
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {message && (
          <div
            className={`friends__message ${successMsg ? "friends__message--success" : "friends__message--error"}`}
            role="alert"
          >
            {message}
          </div>
        )}
      </div>
      </section>
    </div>
  );
}