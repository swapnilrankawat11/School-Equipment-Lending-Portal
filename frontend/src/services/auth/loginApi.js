import API from "../baseURL/api";

export const login = (data) => API.post("/LoginServlet", data);