package com.schoolportal.service;

import com.schoolportal.dao.EquipmentAvailabilityTypesDAO;
import com.schoolportal.dao.EquipmentDAO;
import com.schoolportal.model.Equipment;

public class EquipmentService {

	public boolean updateEquipment(int equipmentId, Equipment newEquipmentData) throws Exception {
		EquipmentDAO dao = new EquipmentDAO();
		Equipment existingEquipmentData = dao.getEquipmentById(equipmentId);

		if (existingEquipmentData == null) {
			throw new Exception("Equipment not found with ID = " + equipmentId);
		}

		int issuedQuantity = existingEquipmentData.getTotalQuantity() - existingEquipmentData.getQuantityLeft();

		// New Total Quantity can't be less than issued
		if (newEquipmentData.getTotalQuantity() < issuedQuantity) {
			throw new Exception("Total quantity cannot be less than issued quantity (" + issuedQuantity + ").");
		}

		int new_quantity_left = newEquipmentData.getTotalQuantity() - issuedQuantity;
		newEquipmentData.setQuantityLeft(new_quantity_left);

		String newStatus = new EquipmentAvailabilityTypesDAO()
				.getAvailabilityStatusNameById(newEquipmentData.getAvailabilityId());

		// Status update logic available -> lent out
		if (issuedQuantity > 0) {
			if (newEquipmentData.getTotalQuantity() == issuedQuantity) {
				if (!newStatus.equalsIgnoreCase("Lent Out")) {
					throw new Exception("All items are issued, status must be 'Lent Out'.");
				}
			} else {
				if (newStatus.equalsIgnoreCase("Damaged")) {
					throw new Exception("Cannot mark equipment as Damaged while items are still issued.");
				}
				if (newStatus.equalsIgnoreCase("Lent Out")) {
					newEquipmentData.setAvailabilityId(
							new EquipmentAvailabilityTypesDAO().getAvailabilityStatusIdByName("Available"));
				}
			}
		} else {
			// nothing is issued
			if (newStatus.equalsIgnoreCase("Lent Out")) {
				throw new Exception("Cannot mark equipment as 'Lent Out' when no items are issued.");
			}
		}

		return dao.updateEquipment(equipmentId, newEquipmentData);
	}
}
