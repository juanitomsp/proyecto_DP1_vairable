import api from "./api";

const statisticsService = {
  getGlobal() {
    return api.get("/statistics/global");
  },

  getUserSummaryByUserId(userId) {
    return api.get(`/statistics/users/${userId}/summary`);
  },

  getRankingPoints(limit = 20) {
    return api.get("/statistics/ranking/points", { params: { limit } });
  },
};

export default statisticsService;