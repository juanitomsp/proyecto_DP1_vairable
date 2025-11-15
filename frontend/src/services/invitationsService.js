import api from "./api";

const invitationsService = {
    sendInvitation(receiverId, gameMode = 'default') {
        const payload = { receiverId, gameMode };
        return api.post('/games/invite', payload);
    },

    updateStatus(id, status) {
        return api.put(`/games/invite/${id}/status?status=${status}`);
    },

    getReceived(userId) {
        return api.get(`/games/invite/received/${userId}`);
    }
};

export default invitationsService;
