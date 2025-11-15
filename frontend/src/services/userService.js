import api from "./api"; 

const userService = {
  getCurrentUser() {
    return api.get("/users/currentUser");
  },
  updateUser(id, userData) {
    return api.put(`/users/${id}`, userData);
  },
};

export default userService;
