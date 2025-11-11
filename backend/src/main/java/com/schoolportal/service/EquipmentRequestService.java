package com.schoolportal.service;

import com.schoolportal.dao.EquipmentAvailabilityTypesDAO;
import com.schoolportal.dao.EquipmentDAO;
import com.schoolportal.dao.EquipmentRequestDAO;
import com.schoolportal.dao.RequestStatusTypesDAO;
import com.schoolportal.model.Equipment;
import com.schoolportal.model.EquipmentRequest;

public class EquipmentRequestService {

	public boolean updateEquipmentRequest(int requestId, EquipmentRequest newRequestData) throws Exception {
		EquipmentRequestDAO requestDAO = new EquipmentRequestDAO();
		EquipmentDAO equipmentDAO = new EquipmentDAO();
		RequestStatusTypesDAO statusDAO = new RequestStatusTypesDAO();
		EquipmentAvailabilityTypesDAO availabilityStatusDAO = new EquipmentAvailabilityTypesDAO();

		EquipmentRequest existingRequest = requestDAO.getEquipmentRequestById(requestId);
		if (existingRequest == null) {
			throw new Exception("No request found with ID: " + requestId);
		}

		Equipment equipment = equipmentDAO.getEquipmentById(existingRequest.getEquipmentId());
		if (equipment == null) {
			throw new Exception("No equipment found with ID: " + existingRequest.getEquipmentId());
		}

		String currentStatus = statusDAO.getRequestStatusTypeById(existingRequest.getRequestStatusId()).getStatus_type()
				.toLowerCase();

		String newStatus = statusDAO.getRequestStatusTypeById(newRequestData.getRequestStatusId()).getStatus_type()
				.toLowerCase();

		if (!isValidStatusTransition(currentStatus, newStatus)) {
			throw new Exception("Invalid status transition: cannot change request from '" + currentStatus + "' to '"
					+ newStatus + "'.");
		}

		if (newStatus.equals("approved")) {

			if (newRequestData.getExpectedReturnDate() == null || newRequestData.getExpectedReturnDate().isEmpty()) {
				throw new Exception("Expected return date is required when approving a request.");
			}

			if (existingRequest.getQuantityRequested() > equipment.getQuantityLeft()) {
				throw new Exception(
						"Insufficient stock. Only " + equipment.getQuantityLeft() + " units are available for issue.");
			}

			int newQtyLeft = equipment.getQuantityLeft() - existingRequest.getQuantityRequested();
			equipmentDAO.updateEquipmentQuantityLeftById(newQtyLeft, equipment.getId());
			newRequestData.setActualReturnDate(null);

			if (newQtyLeft == 0) {
				equipmentDAO.updateAvailabilityStatusById(
						availabilityStatusDAO.getAvailabilityStatusIdByName("Lent Out"), equipment.getId());
			}
		} else if (newStatus.equals("closed")) {
			int newQtyLeft = equipment.getQuantityLeft() + existingRequest.getQuantityRequested();
			if (newQtyLeft > 0) {
				equipmentDAO.updateAvailabilityStatusById(
						availabilityStatusDAO.getAvailabilityStatusIdByName("Available"), equipment.getId());
			}
			equipmentDAO.updateEquipmentQuantityLeftById(newQtyLeft, equipment.getId());
			newRequestData.setExpectedReturnDate(existingRequest.getExpectedReturnDate());
			newRequestData.setActualReturnDate(java.time.LocalDate.now().toString());
		}

		return requestDAO.updateEquipmentRequest(requestId, newRequestData);
	}

	private boolean isValidStatusTransition(String currentStatus, String newStatus) {

		switch (currentStatus) {
		case "pending":
			return newStatus.equals("approved") || newStatus.equals("rejected") || newStatus.equals("pending");
		case "approved":
			return newStatus.equals("closed");
		case "rejected":
			return newStatus.equals("rejected");
		case "closed":
			return newStatus.equals("closed");
		default:
			return false;
		}
	}
}
