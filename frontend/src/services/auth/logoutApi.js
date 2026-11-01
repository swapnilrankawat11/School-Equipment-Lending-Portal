import API from "../baseURL/api";

export const logoutApi = async () => API.post("/LogoutServlet");
