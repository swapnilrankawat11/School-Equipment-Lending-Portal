import API from "../baseURL/api";

// Added optional statusFilterId param (default to empty string)
export const getRequestsByStudent = (studentId, statusFilterId) =>
  API.get(`/EquipmentRequestServlet/${studentId}`, {
    params: { statusFilterId },
  });

export const cancelRequest = (requestId) =>
  API.delete(`/EquipmentRequestServlet/${requestId}`);
