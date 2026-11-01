import API from "../baseURL/api";

export const getUserTypes = () => API.get("/UserTypesServlet");