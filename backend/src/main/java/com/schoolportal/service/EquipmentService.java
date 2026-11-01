
package com.schoolportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.EquipmentDTO;
import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;
import com.schoolportal.repository.EquipmentCategoryRepository;
import com.schoolportal.repository.EquipmentRepository;

@Service
public class EquipmentService {

	private final EquipmentRepository equipmentRepository;
	private final EquipmentAvailabilityTypeRepository availabilityRepository;
	private final EquipmentCategoryRepository categoryRepository;

	public EquipmentService(EquipmentRepository equipmentRepository,
			EquipmentAvailabilityTypeRepository availabilityRepository,
			EquipmentCategoryRepository categoryRepository) {

		this.equipmentRepository = equipmentRepository;
		this.availabilityRepository = availabilityRepository;
		this.categoryRepository = categoryRepository;
	}

	// GET: Retrieve all equipment
	@Transactional(readOnly = true)
	public List<EquipmentDTO> getAllEquipments() {

		List<Equipment> equipments = equipmentRepository.findAllWithDetails();

		return equipments.stream().map(this::convertToDTO).toList();
	}

	// POST: Add new equipment
	@Transactional
	public boolean addEquipment(EquipmentDTO dto) {

		EquipmentCategory category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Invalid equipment category."));

		EquipmentAvailabilityType availability = availabilityRepository.findById(dto.getAvailabilityId())
				.orElseThrow(() -> new RuntimeException("Invalid availability status."));

		if (dto.getTotalQuantity() == null || dto.getTotalQuantity() < 0) {
			throw new RuntimeException("Total quantity cannot be negative.");
		}

		if (dto.getQuantityLeft() != null && !dto.getQuantityLeft().equals(dto.getTotalQuantity())) {
			throw new RuntimeException("New equipment quantity left must equal total quantity.");
		}

		Equipment equipment = new Equipment();

		equipment.setName(dto.getName());
		equipment.setCategory(category);
		equipment.setAvailabilityType(availability);
		equipment.setTotalQuantity(dto.getTotalQuantity());

		// Newly added equipment has no issued items
		equipment.setQuantityLeft(dto.getTotalQuantity());

		equipment.setDescription(dto.getDescription());
		equipment.setCreatedBy(dto.getCreatedBy());
		equipment.setModifiedBy(dto.getModifiedBy());

		equipmentRepository.save(equipment);

		return true;
	}

	// PUT: Update existing equipment
	@Transactional
	public boolean updateEquipment(int equipmentId, EquipmentDTO dto) {

		Equipment existingEquipment = equipmentRepository.findById(equipmentId)
				.orElseThrow(() -> new RuntimeException("Equipment not found with ID = " + equipmentId));

		Integer currentQuantityLeft = existingEquipment.getQuantityLeft();

		if (currentQuantityLeft == null) {
			throw new RuntimeException("Equipment stock quantity is not configured.");
		}

		int issuedQuantity = existingEquipment.getTotalQuantity() - currentQuantityLeft;

		if (dto.getTotalQuantity() == null || dto.getTotalQuantity() < 0) {
			throw new RuntimeException("Total quantity cannot be negative.");
		}

		if (dto.getTotalQuantity() < issuedQuantity) {
			throw new RuntimeException("Total quantity cannot be less than issued quantity (" + issuedQuantity + ").");
		}

		int newQuantityLeft = dto.getTotalQuantity() - issuedQuantity;

		EquipmentAvailabilityType newAvailability = availabilityRepository.findById(dto.getAvailabilityId())
				.orElseThrow(() -> new RuntimeException("Invalid availability status."));

		String newStatus = newAvailability.getAvailabilityStatus();

		if (issuedQuantity > 0) {

			if (dto.getTotalQuantity() == issuedQuantity) {

				if (!newStatus.equalsIgnoreCase("Lent Out")) {
					throw new RuntimeException("All items are issued, status must be 'Lent Out'.");
				}

			} else {

				if (newStatus.equalsIgnoreCase("Damaged")) {
					throw new RuntimeException("Cannot mark equipment as Damaged while items are still issued.");
				}

				if (newStatus.equalsIgnoreCase("Lent Out")) {

					newAvailability = availabilityRepository.findByAvailabilityStatusIgnoreCase("Available")
							.orElseThrow(() -> new RuntimeException("Available status not found in database."));
				}
			}

		} else {

			if (newStatus.equalsIgnoreCase("Lent Out")) {
				throw new RuntimeException("Cannot mark equipment as 'Lent Out' when no items are issued.");
			}
		}

		EquipmentCategory category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Invalid equipment category."));

		// Update existing managed entity
		existingEquipment.setName(dto.getName());
		existingEquipment.setCategory(category);
		existingEquipment.setAvailabilityType(newAvailability);
		existingEquipment.setTotalQuantity(dto.getTotalQuantity());
		existingEquipment.setQuantityLeft(newQuantityLeft);
		existingEquipment.setDescription(dto.getDescription());

		if (dto.getModifiedBy() != null) {
			existingEquipment.setModifiedBy(dto.getModifiedBy());
		}

		equipmentRepository.save(existingEquipment);

		return true;
	}

	// DELETE: Delete equipment
	@Transactional
	public boolean deleteEquipment(int equipmentId) {

		if (!equipmentRepository.existsById(equipmentId)) {
			return false;
		}

		equipmentRepository.deleteById(equipmentId);

		return true;
	}

	// ENTITY TO DTO MAPPING
	private EquipmentDTO convertToDTO(Equipment equipment) {

		EquipmentDTO dto = new EquipmentDTO();

		dto.setId(equipment.getId());

		dto.setName(equipment.getName());

		dto.setCategoryId(equipment.getCategory().getId());
		dto.setCategoryName(equipment.getCategory().getCategoryName());

		dto.setAvailabilityId(equipment.getAvailabilityType().getId());
		dto.setAvailabilityStatus(equipment.getAvailabilityType().getAvailabilityStatus());

		dto.setTotalQuantity(equipment.getTotalQuantity());
		dto.setQuantityLeft(equipment.getQuantityLeft());

		dto.setDescription(equipment.getDescription());

		dto.setCreatedBy(equipment.getCreatedBy());
		dto.setModifiedBy(equipment.getModifiedBy());

		dto.setCreatedAt(equipment.getCreatedAt());
		dto.setModifiedAt(equipment.getModifiedAt());

		return dto;
	}
}
