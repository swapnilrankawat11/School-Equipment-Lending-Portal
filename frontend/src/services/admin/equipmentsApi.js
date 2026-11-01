import API from "../../services/baseURL/api";

export const getEquipments = () => API.get("/EquipmentServlet");
export const addEquipment = (data) => API.post("/EquipmentServlet", data);
export const updateEquipment = (id, data) =>
  API.put(`/EquipmentServlet/${id}`, data);
export const deleteEquipment = (id) => API.delete(`/EquipmentServlet/${id}`);
