import API from "../baseURL/api";

export const getCategories = () => API.get("/EquipmentCategoryServlet");
export const addCategory = (data) =>
  API.post("/EquipmentCategoryServlet", data);
export const updateCategory = (id, data) =>
  API.put(`/EquipmentCategoryServlet/${id}`, data);
export const deleteCategory = (id) =>
  API.delete(`/EquipmentCategoryServlet/${id}`);
