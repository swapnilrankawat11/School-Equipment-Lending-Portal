import API from "../baseURL/api";

export const getSessionInfo = () => API.get("/SessionInfoServlet");