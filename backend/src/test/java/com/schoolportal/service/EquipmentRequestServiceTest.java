
package com.schoolportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class EquipmentRequestServiceTest {

	@Mock
	private EquipmentRequestRepository requestRepository;

	@Mock
	private EquipmentRepository equipmentRepository;

	@Mock
	private RequestStatusTypeRepository statusRepository;

	@Mock
	private EquipmentAvailabilityTypeRepository availabilityRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private EquipmentRequestService requestService;

	private Equipment equipment;
	private RequestStatusType pending;
	private RequestStatusType approved;
	private RequestStatusType rejected;
	private RequestStatusType closed;
	private EquipmentAvailabilityType available;
	private EquipmentAvailabilityType lentOut;

	@BeforeEach
	void setUp() {

		equipment = new Equipment();
		equipment.setId(1);
		equipment.setName("Laptop");
		equipment.setTotalQuantity(10);
		equipment.setQuantityLeft(5);

		available = new EquipmentAvailabilityType();
		available.setId(1);
		available.setAvailabilityStatus("Available");

		lentOut = new EquipmentAvailabilityType();
		lentOut.setId(2);
		lentOut.setAvailabilityStatus("Lent Out");

		pending = createStatus(1, "Pending");
		approved = createStatus(2, "Approved");
		rejected = createStatus(3, "Rejected");
		closed = createStatus(4, "Closed");
	}

	private RequestStatusType createStatus(int id, String name) {

		RequestStatusType status = new RequestStatusType();
		status.setId(id);
		status.setStatusType(name);
		return status;
	}

	private EquipmentRequestDTO createDTO() {

		EquipmentRequestDTO dto = new EquipmentRequestDTO();

		dto.setEquipmentId(1);
		dto.setQuantityRequested(2);
		dto.setPurpose("Academic project");
		dto.setCreatedBy(10);
		dto.setModifiedBy(10);

		return dto;
	}

	private EquipmentRequest createRequest(int id, RequestStatusType status, int quantity) {

		EquipmentRequest request = new EquipmentRequest();

		request.setId(id);
		request.setEquipment(equipment);
		request.setRequestStatus(status);
		request.setQuantityRequested(quantity);
		request.setPurpose("Academic project");
		request.setCreatedBy(10);
		request.setModifiedBy(10);

		return request;
	}

	private User createUser() {

		User user = new User();
		user.setId(10);
		user.setFirstName("John");
		user.setLastName("Smith");

		return user;
	}

	// CREATE REQUEST TESTS

	@Test
	void createEquipmentRequest_validData_shouldSaveRequest() {

		EquipmentRequestDTO dto = createDTO();

		when(equipmentRepository.findById(1)).thenReturn(Optional.of(equipment));

		when(statusRepository.findAll()).thenReturn(List.of(pending));

		when(requestRepository.save(any(EquipmentRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

		assertTrue(requestService.createEquipmentRequest(dto));

		ArgumentCaptor<EquipmentRequest> captor = ArgumentCaptor.forClass(EquipmentRequest.class);

		verify(requestRepository).save(captor.capture());

		EquipmentRequest saved = captor.getValue();

		assertEquals(equipment, saved.getEquipment());
		assertEquals(pending, saved.getRequestStatus());
		assertEquals(2, saved.getQuantityRequested());
		assertEquals("Academic project", saved.getPurpose());
		assertEquals(10, saved.getCreatedBy());
		assertEquals(10, saved.getModifiedBy());
	}

	@Test
	void createEquipmentRequest_nullEquipmentId_shouldThrowException() {

		EquipmentRequestDTO dto = createDTO();
		dto.setEquipmentId(null);

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.createEquipmentRequest(dto));

		assertEquals("Equipment ID is required.", exception.getMessage());

		verifyNoInteractions(equipmentRepository, statusRepository, requestRepository);
	}

	@Test
	void createEquipmentRequest_nullQuantity_shouldThrowException() {

		EquipmentRequestDTO dto = createDTO();
		dto.setQuantityRequested(null);

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.createEquipmentRequest(dto));

		assertEquals("Requested quantity must be greater than zero.", exception.getMessage());
	}

	@Test
	void createEquipmentRequest_zeroQuantity_shouldThrowException() {

		EquipmentRequestDTO dto = createDTO();
		dto.setQuantityRequested(0);

		assertThrows(RuntimeException.class, () -> requestService.createEquipmentRequest(dto));
	}

	@Test
	void createEquipmentRequest_negativeQuantity_shouldThrowException() {

		EquipmentRequestDTO dto = createDTO();
		dto.setQuantityRequested(-1);

		assertThrows(RuntimeException.class, () -> requestService.createEquipmentRequest(dto));
	}

	@Test
	void createEquipmentRequest_equipmentNotFound_shouldThrowException() {

		when(equipmentRepository.findById(1)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.createEquipmentRequest(createDTO()));

		assertEquals("Equipment not found.", exception.getMessage());
	}

	@Test
	void createEquipmentRequest_pendingStatusMissing_shouldThrowException() {

		when(equipmentRepository.findById(1)).thenReturn(Optional.of(equipment));

		when(statusRepository.findAll()).thenReturn(List.of(approved, rejected));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.createEquipmentRequest(createDTO()));

		assertEquals("Pending status not found.", exception.getMessage());
	}

	@Test
	void createEquipmentRequest_createdByMissing_shouldThrowException() {

		EquipmentRequestDTO dto = createDTO();
		dto.setCreatedBy(null);

		when(equipmentRepository.findById(1)).thenReturn(Optional.of(equipment));

		when(statusRepository.findAll()).thenReturn(List.of(pending));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.createEquipmentRequest(dto));

		assertEquals("Created by user ID is required.", exception.getMessage());

		verify(requestRepository, never()).save(any());
	}

	// GET REQUESTS BY STATUS TESTS

	@Test
	void getEquipmentRequestsByStatusType_shouldReturnMappedDTOs() {

		EquipmentRequest request = createRequest(1, pending, 2);

		when(requestRepository.findRequestsByStatusTypeId(1)).thenReturn(List.of(request));

		when(userRepository.findById(10)).thenReturn(Optional.of(createUser()));

		List<EquipmentRequestDTO> result = requestService.getEquipmentRequestsByStatusType(1);

		assertEquals(1, result.size());

		EquipmentRequestDTO dto = result.get(0);

		assertEquals(1, dto.getId());
		assertEquals(1, dto.getEquipmentId());
		assertEquals("Laptop", dto.getEquipmentName());
		assertEquals(2, dto.getQuantityRequested());
		assertEquals("Academic project", dto.getPurpose());
		assertEquals(1, dto.getRequestStatusId());
		assertEquals("Pending", dto.getRequestStatusName());
		assertEquals("John Smith", dto.getCreatedByName());
		assertEquals("John Smith", dto.getModifiedByName());
	}

	@Test
	void getEquipmentRequestsByStatusType_emptyList_shouldReturnEmptyList() {

		when(requestRepository.findRequestsByStatusTypeId(1)).thenReturn(List.of());

		List<EquipmentRequestDTO> result = requestService.getEquipmentRequestsByStatusType(1);

		assertTrue(result.isEmpty());
	}

	// GET REQUESTS BY STUDENT AND STATUS TESTS

	@Test
	void getEquipmentRequestsByStudentAndStatus_shouldReturnMappedDTOs() {

		EquipmentRequest request = createRequest(1, pending, 2);

		when(requestRepository.findRequestsByStudentAndStatus(10, 1)).thenReturn(List.of(request));

		when(userRepository.findById(10)).thenReturn(Optional.of(createUser()));

		List<EquipmentRequestDTO> result = requestService.getEquipmentRequestsByStudentAndStatus(10, 1);

		assertEquals(1, result.size());
		assertEquals("Laptop", result.get(0).getEquipmentName());
		assertEquals("Pending", result.get(0).getRequestStatusName());

		verify(requestRepository).findRequestsByStudentAndStatus(10, 1);
	}

	@Test
	void getEquipmentRequestsByStudentAndStatus_emptyList_shouldReturnEmptyList() {

		when(requestRepository.findRequestsByStudentAndStatus(10, 1)).thenReturn(List.of());

		List<EquipmentRequestDTO> result = requestService.getEquipmentRequestsByStudentAndStatus(10, 1);

		assertTrue(result.isEmpty());
	}

	// UPDATE REQUEST TESTS

	private void mockExistingRequest(EquipmentRequest request, int requestId) {

		when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

		when(equipmentRepository.findById(1)).thenReturn(Optional.of(equipment));
	}

	@Test
	void updateEquipmentRequest_validApproval_shouldReduceStock() {

		EquipmentRequest request = createRequest(1, pending, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);
		dto.setExpectedReturnDate(LocalDate.now().plusDays(7));

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

		when(requestRepository.save(any(EquipmentRequest.class))).thenReturn(request);

		assertTrue(requestService.updateEquipmentRequest(1, dto));

		assertEquals(3, equipment.getQuantityLeft());
		assertEquals(approved, request.getRequestStatus());
		assertEquals(dto.getExpectedReturnDate(), request.getExpectedReturnDate());

		verify(equipmentRepository).save(equipment);
		verify(requestRepository).save(request);
	}

	@Test
	void updateEquipmentRequest_requestNotFound_shouldThrowException() {

		when(requestRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(99, createDTO()));

		assertEquals("No request found with ID: 99", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_equipmentNotFound_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 2);

		when(requestRepository.findById(1)).thenReturn(Optional.of(request));

		when(equipmentRepository.findById(1)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, createDTO()));

		assertEquals("Equipment not found.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_nullStatusId_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 2);
		EquipmentRequestDTO dto = createDTO();

		dto.setRequestStatusId(null);

		mockExistingRequest(request, 1);

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Request status is required.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_invalidStatusId_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(99);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Invalid request status.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_invalidStatusTransition_shouldThrowException() {

		EquipmentRequest request = createRequest(1, approved, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(1);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(1)).thenReturn(Optional.of(pending));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertTrue(exception.getMessage().contains("Invalid status transition"));
	}

	@Test
	void updateEquipmentRequest_approvalWithoutReturnDate_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Expected return date is required when approving a request.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_nullStock_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 2);
		equipment.setQuantityLeft(null);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);
		dto.setExpectedReturnDate(LocalDate.now().plusDays(7));

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Equipment stock quantity is not configured.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_insufficientStock_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 6);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);
		dto.setExpectedReturnDate(LocalDate.now().plusDays(7));

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Insufficient stock. Only 5 units are available for issue.", exception.getMessage());

		assertEquals(5, equipment.getQuantityLeft());
	}

	@Test
	void updateEquipmentRequest_allStockIssued_shouldSetLentOutStatus() {

		EquipmentRequest request = createRequest(1, pending, 5);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);
		dto.setExpectedReturnDate(LocalDate.now().plusDays(7));

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Lent Out")).thenReturn(Optional.of(lentOut));

		assertTrue(requestService.updateEquipmentRequest(1, dto));

		assertEquals(0, equipment.getQuantityLeft());
		assertEquals(lentOut, equipment.getAvailabilityType());
	}

	@Test
	void updateEquipmentRequest_lentOutStatusMissing_shouldThrowException() {

		EquipmentRequest request = createRequest(1, pending, 5);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(2);
		dto.setExpectedReturnDate(LocalDate.now().plusDays(7));

		mockExistingRequest(request, 1);

		when(statusRepository.findById(2)).thenReturn(Optional.of(approved));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Lent Out")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Lent Out availability status not found.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_rejection_shouldUpdateStatusWithoutChangingStock() {

		EquipmentRequest request = createRequest(1, pending, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(3);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(3)).thenReturn(Optional.of(rejected));

		assertTrue(requestService.updateEquipmentRequest(1, dto));

		assertEquals(5, equipment.getQuantityLeft());
		assertEquals(rejected, request.getRequestStatus());
	}

	@Test
	void updateEquipmentRequest_closingRequest_shouldRestoreStockAndSetDates() {

		EquipmentRequest request = createRequest(1, approved, 2);

		LocalDate expectedDate = LocalDate.now().plusDays(7);
		request.setExpectedReturnDate(expectedDate);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(4);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(4)).thenReturn(Optional.of(closed));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Available")).thenReturn(Optional.of(available));

		assertTrue(requestService.updateEquipmentRequest(1, dto));

		assertEquals(7, equipment.getQuantityLeft());
		assertEquals(available, equipment.getAvailabilityType());
		assertEquals(expectedDate, request.getExpectedReturnDate());
		assertEquals(LocalDate.now(), request.getActualReturnDate());
		assertEquals(closed, request.getRequestStatus());
	}

	@Test
	void updateEquipmentRequest_closingWithoutStockConfigured_shouldThrowException() {

		EquipmentRequest request = createRequest(1, approved, 2);
		equipment.setQuantityLeft(null);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(4);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(4)).thenReturn(Optional.of(closed));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Equipment stock quantity is not configured.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_availableStatusMissingWhenClosing_shouldThrowException() {

		EquipmentRequest request = createRequest(1, approved, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(4);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(4)).thenReturn(Optional.of(closed));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Available")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> requestService.updateEquipmentRequest(1, dto));

		assertEquals("Available availability status not found.", exception.getMessage());
	}

	@Test
	void updateEquipmentRequest_modifiedByProvided_shouldUpdateAuditUser() {

		EquipmentRequest request = createRequest(1, pending, 2);

		EquipmentRequestDTO dto = createDTO();
		dto.setRequestStatusId(3);
		dto.setModifiedBy(20);

		mockExistingRequest(request, 1);

		when(statusRepository.findById(3)).thenReturn(Optional.of(rejected));

		assertTrue(requestService.updateEquipmentRequest(1, dto));

		assertEquals(20, request.getModifiedBy());
	}

	// DELETE REQUEST TESTS

	@Test
	void deleteEquipmentRequest_existingRequest_shouldDeleteAndReturnTrue() {

		when(requestRepository.existsById(1)).thenReturn(true);

		assertTrue(requestService.deleteEquipmentRequest(1));

		verify(requestRepository).deleteById(1);
	}

	@Test
	void deleteEquipmentRequest_missingRequest_shouldReturnFalse() {

		when(requestRepository.existsById(99)).thenReturn(false);

		assertFalse(requestService.deleteEquipmentRequest(99));

		verify(requestRepository, never()).deleteById(anyInt());
	}
}
