import API from "../baseURL/api";

export const getRequestsByStatusType = (statusTypeId) => API.get(`/EquipmentRequestServlet/${statusTypeId}`);
export const updateRequest = (id, data) => API.put(`/EquipmentRequestServlet/${id}`, data);