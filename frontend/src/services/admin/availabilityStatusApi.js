import API from "../baseURL/api";

export const getAvailabilityStatuses = () => API.get("/EquipmentAvailabilityTypesServlet");