package com.schoolportal.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.entity.EquipmentRequest;
import com.schoolportal.entity.RequestStatusType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentRequestRepositoryTest {

	@Autowired
	private EquipmentRequestRepository repository;

	@Autowired
	private EquipmentRepository equipmentRepository;

	@Autowired
	private EquipmentCategoryRepository categoryRepository;

	@Autowired
	private EquipmentAvailabilityTypeRepository availabilityTypeRepository;

	@Autowired
	private RequestStatusTypeRepository statusRepository;

	private Equipment createEquipment() {
		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName("Electronics");
		category = categoryRepository.save(category);

		EquipmentAvailabilityType availability = new EquipmentAvailabilityType();
		availability.setAvailabilityStatus("Available");
		availability = availabilityTypeRepository.save(availability);

		Equipment equipment = new Equipment();
		equipment.setName("Laptop");
		equipment.setCategory(category);
		equipment.setAvailabilityType(availability);
		equipment.setTotalQuantity(10);
		equipment.setQuantityLeft(8);

		return equipmentRepository.save(equipment);
	}

	private RequestStatusType createStatus(String statusType) {
		RequestStatusType status = new RequestStatusType();
		status.setStatusType(statusType);
		return statusRepository.save(status);
	}

	private EquipmentRequest createRequest(Equipment equipment, RequestStatusType status, int studentId,
			String purpose) {

		EquipmentRequest request = new EquipmentRequest();
		request.setEquipment(equipment);
		request.setQuantityRequested(2);
		request.setPurpose(purpose);
		request.setRequestStatus(status);
		request.setExpectedReturnDate(LocalDate.now().plusDays(7));
		request.setCreatedBy(studentId);

		return repository.save(request);
	}

	@Test
	void save_shouldPersistEquipmentRequest() {
		Equipment equipment = createEquipment();
		RequestStatusType status = createStatus("Pending");

		EquipmentRequest saved = createRequest(equipment, status, 101, "Academic project");

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getQuantityRequested()).isEqualTo(2);
		assertThat(saved.getPurpose()).isEqualTo("Academic project");
		assertThat(saved.getCreatedBy()).isEqualTo(101);
	}

	@Test
	void findRequestsByStatusTypeId_shouldReturnMatchingRequests() {
		Equipment equipment = createEquipment();
		RequestStatusType pending = createStatus("Pending");
		RequestStatusType approved = createStatus("Approved");

		createRequest(equipment, pending, 101, "Project work");
		createRequest(equipment, approved, 102, "Lab work");

		List<EquipmentRequest> result = repository.findRequestsByStatusTypeId(pending.getId());

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getRequestStatus().getId()).isEqualTo(pending.getId());
		assertThat(result.get(0).getEquipment().getName()).isEqualTo("Laptop");
	}

	@Test
	void findRequestsByStatusTypeId_shouldReturnEmptyWhenNoMatch() {
		RequestStatusType pending = createStatus("Pending");

		assertThat(repository.findRequestsByStatusTypeId(pending.getId())).isEmpty();
	}

	@Test
	void findRequestWithDetailsById_shouldReturnRequestWithDetails() {
		Equipment equipment = createEquipment();
		RequestStatusType status = createStatus("Pending");

		EquipmentRequest saved = createRequest(equipment, status, 101, "Academic project");

		EquipmentRequest result = repository.findRequestWithDetailsById(saved.getId());

		assertThat(result).isNotNull();
		assertThat(result.getEquipment().getName()).isEqualTo("Laptop");
		assertThat(result.getRequestStatus().getStatusType()).isEqualTo("Pending");
	}

	@Test
	void findRequestWithDetailsById_shouldReturnNullWhenNotFound() {
		assertThat(repository.findRequestWithDetailsById(999)).isNull();
	}

	@Test
	void findRequestsByStudentAndStatus_shouldReturnMatchingRequests() {
		Equipment equipment = createEquipment();
		RequestStatusType pending = createStatus("Pending");
		RequestStatusType approved = createStatus("Approved");

		createRequest(equipment, pending, 101, "Project work");
		createRequest(equipment, approved, 101, "Lab work");
		createRequest(equipment, pending, 102, "Research work");

		List<EquipmentRequest> result = repository.findRequestsByStudentAndStatus(101, pending.getId());

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getCreatedBy()).isEqualTo(101);
		assertThat(result.get(0).getRequestStatus().getId()).isEqualTo(pending.getId());
	}

	@Test
	void findRequestsByStudentAndStatus_shouldReturnEmptyWhenNoMatch() {
		Equipment equipment = createEquipment();
		RequestStatusType pending = createStatus("Pending");

		createRequest(equipment, pending, 101, "Project work");

		assertThat(repository.findRequestsByStudentAndStatus(999, pending.getId())).isEmpty();
	}

	@Test
	void findAll_shouldReturnAllRequests() {
		Equipment equipment = createEquipment();
		RequestStatusType status = createStatus("Pending");

		createRequest(equipment, status, 101, "Project work");
		createRequest(equipment, status, 102, "Lab work");

		assertThat(repository.findAll()).hasSize(2);
	}

	@Test
	void delete_shouldRemoveRequest() {
		Equipment equipment = createEquipment();
		RequestStatusType status = createStatus("Pending");

		EquipmentRequest saved = createRequest(equipment, status, 101, "Project work");

		repository.deleteById(saved.getId());

		assertThat(repository.findById(saved.getId())).isEmpty();
	}
}