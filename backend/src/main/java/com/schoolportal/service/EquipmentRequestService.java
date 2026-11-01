
package com.schoolportal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.EquipmentRequestDTO;
import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentRequest;
import com.schoolportal.entity.RequestStatusType;
import com.schoolportal.entity.User;

import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;
import com.schoolportal.repository.EquipmentRepository;
import com.schoolportal.repository.EquipmentRequestRepository;
import com.schoolportal.repository.RequestStatusTypeRepository;
import com.schoolportal.repository.UserRepository;

@Service
public class EquipmentRequestService {

	private final EquipmentRequestRepository requestRepository;
	private final EquipmentRepository equipmentRepository;
	private final RequestStatusTypeRepository statusRepository;
	private final EquipmentAvailabilityTypeRepository availabilityRepository;
	private final UserRepository userRepository;

	public EquipmentRequestService(EquipmentRequestRepository requestRepository,
			EquipmentRepository equipmentRepository, RequestStatusTypeRepository statusRepository,
			EquipmentAvailabilityTypeRepository availabilityRepository, UserRepository userRepository) {

		this.requestRepository = requestRepository;
		this.equipmentRepository = equipmentRepository;
		this.statusRepository = statusRepository;
		this.availabilityRepository = availabilityRepository;
		this.userRepository = userRepository;
	}

	// CREATE: Create a new equipment request
	@Transactional
	public boolean createEquipmentRequest(EquipmentRequestDTO dto) {

		if (dto.getEquipmentId() == null) {
			throw new RuntimeException("Equipment ID is required.");
		}

		if (dto.getQuantityRequested() == null || dto.getQuantityRequested() <= 0) {
			throw new RuntimeException("Requested quantity must be greater than zero.");
		}

		Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
				.orElseThrow(() -> new RuntimeException("Equipment not found."));

		// New requests start with Pending status
		RequestStatusType pendingStatus = statusRepository.findAll().stream()
				.filter(status -> status.getStatusType().equalsIgnoreCase("Pending")).findFirst()
				.orElseThrow(() -> new RuntimeException("Pending status not found."));

		EquipmentRequest request = new EquipmentRequest();

		request.setEquipment(equipment);
		request.setRequestStatus(pendingStatus);
		request.setQuantityRequested(dto.getQuantityRequested());
		request.setPurpose(dto.getPurpose());

		if (dto.getCreatedBy() == null) {
			throw new RuntimeException("Created by user ID is required.");
		}

		request.setCreatedBy(dto.getCreatedBy());
		request.setModifiedBy(dto.getCreatedBy());
		requestRepository.save(request);

		return true;
	}

	// GET: Retrieve requests by status
	@Transactional(readOnly = true)
	public List<EquipmentRequestDTO> getEquipmentRequestsByStatusType(int statusId) {

		List<EquipmentRequest> requests = requestRepository.findRequestsByStatusTypeId(statusId);

		return requests.stream().map(this::convertToDTO).toList();
	}

	// GET: Retrieve requests by student and status
	@Transactional(readOnly = true)
	public List<EquipmentRequestDTO> getEquipmentRequestsByStudentAndStatus(int studentId, int statusId) {

		List<EquipmentRequest> requests = requestRepository.findRequestsByStudentAndStatus(studentId, statusId);

		return requests.stream().map(this::convertToDTO).toList();
	}

	// PUT: Update an existing equipment request
	@Transactional
	public boolean updateEquipmentRequest(int requestId, EquipmentRequestDTO dto) {

		// 1. Find existing request
		EquipmentRequest existingRequest = requestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("No request found with ID: " + requestId));

		// 2. Find associated equipment
		Equipment equipment = equipmentRepository.findById(existingRequest.getEquipment().getId())
				.orElseThrow(() -> new RuntimeException("Equipment not found."));

		// 3. Find new request status
		if (dto.getRequestStatusId() == null) {
			throw new RuntimeException("Request status is required.");
		}

		RequestStatusType newStatusType = statusRepository.findById(dto.getRequestStatusId())
				.orElseThrow(() -> new RuntimeException("Invalid request status."));

		String currentStatus = existingRequest.getRequestStatus().getStatusType().toLowerCase(Locale.ROOT);

		String newStatus = newStatusType.getStatusType().toLowerCase(Locale.ROOT);

		// 4. Validate status transition
		if (!isValidStatusTransition(currentStatus, newStatus)) {
			throw new RuntimeException("Invalid status transition: cannot change request from '" + currentStatus
					+ "' to '" + newStatus + "'.");
		}

		// 5. Handle approval
		if (newStatus.equals("approved")) {

			if (dto.getExpectedReturnDate() == null) {
				throw new RuntimeException("Expected return date is required when approving a request.");
			}

			Integer quantityLeft = equipment.getQuantityLeft();

			if (quantityLeft == null) {
				throw new RuntimeException("Equipment stock quantity is not configured.");
			}

			if (existingRequest.getQuantityRequested() > quantityLeft) {
				throw new RuntimeException(
						"Insufficient stock. Only " + quantityLeft + " units are available for issue.");
			}

			int newQtyLeft = quantityLeft - existingRequest.getQuantityRequested();

			equipment.setQuantityLeft(newQtyLeft);

			dto.setActualReturnDate(null);

			// If all stock is issued, mark equipment as Lent Out
			if (newQtyLeft == 0) {

				EquipmentAvailabilityType lentOutStatus = availabilityRepository
						.findByAvailabilityStatusIgnoreCase("Lent Out")
						.orElseThrow(() -> new RuntimeException("Lent Out availability status not found."));

				equipment.setAvailabilityType(lentOutStatus);
			}
		}

		// 6. Handle closing a request
		else if (newStatus.equals("closed")) {

			Integer quantityLeft = equipment.getQuantityLeft();

			if (quantityLeft == null) {
				throw new RuntimeException("Equipment stock quantity is not configured.");
			}

			int newQtyLeft = quantityLeft + existingRequest.getQuantityRequested();

			equipment.setQuantityLeft(newQtyLeft);

			if (newQtyLeft > 0) {

				EquipmentAvailabilityType availableStatus = availabilityRepository
						.findByAvailabilityStatusIgnoreCase("Available")
						.orElseThrow(() -> new RuntimeException("Available availability status not found."));

				equipment.setAvailabilityType(availableStatus);
			}

			// Preserve the previously expected return date
			dto.setExpectedReturnDate(existingRequest.getExpectedReturnDate());

			dto.setActualReturnDate(LocalDate.now());
		}

		// 7. Update existing request
		existingRequest.setRequestStatus(newStatusType);
		existingRequest.setExpectedReturnDate(dto.getExpectedReturnDate());
		existingRequest.setActualReturnDate(dto.getActualReturnDate());

		if (dto.getModifiedBy() != null) {
			existingRequest.setModifiedBy(dto.getModifiedBy());
		}

		// 8. Save changes
		equipmentRepository.save(equipment);
		requestRepository.save(existingRequest);

		return true;
	}

	// DELETE: Delete an equipment request
	@Transactional
	public boolean deleteEquipmentRequest(int requestId) {

		if (!requestRepository.existsById(requestId)) {
			return false;
		}

		requestRepository.deleteById(requestId);

		return true;
	}

	// ENTITY TO DTO MAPPING
	private EquipmentRequestDTO convertToDTO(EquipmentRequest request) {

		EquipmentRequestDTO dto = new EquipmentRequestDTO();

		dto.setId(request.getId());

		dto.setEquipmentId(request.getEquipment().getId());
		dto.setEquipmentName(request.getEquipment().getName());

		dto.setQuantityRequested(request.getQuantityRequested());
		dto.setPurpose(request.getPurpose());

		dto.setRequestStatusId(request.getRequestStatus().getId());
		dto.setRequestStatusName(request.getRequestStatus().getStatusType());

		dto.setExpectedReturnDate(request.getExpectedReturnDate());
		dto.setActualReturnDate(request.getActualReturnDate());

		dto.setCreatedBy(request.getCreatedBy());
		dto.setModifiedBy(request.getModifiedBy());

		dto.setCreatedAt(request.getCreatedAt());
		dto.setModifiedAt(request.getModifiedAt());

		dto.setCreatedByName(getUserFullName(request.getCreatedBy()));
		dto.setModifiedByName(getUserFullName(request.getModifiedBy()));

		return dto;
	}

	// Get audit user's full name
	private String getUserFullName(int userId) {

		return userRepository.findById(userId).map(user -> user.getFirstName() + " " + user.getLastName()).orElse(null);
	}

	// Validate request status transitions
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
