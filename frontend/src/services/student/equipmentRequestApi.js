import API from "../baseURL/api";

export const createEquipmentRequest = (data) =>
  API.post("/EquipmentRequestServlet", data);
