import API from "../baseURL/api";

export const signup = (data) => API.post("/SignupServlet", data);