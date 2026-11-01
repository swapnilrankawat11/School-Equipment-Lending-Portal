import API from "../baseURL/api";

export const getRequestStatusTypes = () => API.get("/RequestStatusTypesServlet");