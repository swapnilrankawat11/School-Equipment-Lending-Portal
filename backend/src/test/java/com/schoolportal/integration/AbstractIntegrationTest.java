package com.schoolportal.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.entity.EquipmentRequest;
import com.schoolportal.entity.RequestStatusType;
import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;
import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;
import com.schoolportal.repository.EquipmentCategoryRepository;
import com.schoolportal.repository.EquipmentRepository;
import com.schoolportal.repository.EquipmentRequestRepository;
import com.schoolportal.repository.RequestStatusTypeRepository;
import com.schoolportal.repository.UserRepository;
import com.schoolportal.repository.UserTypeRepository;

@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest {

	@Autowired
	protected EquipmentRepository equipmentRepository;
	@Autowired
	protected EquipmentCategoryRepository categoryRepository;
	@Autowired
	protected EquipmentAvailabilityTypeRepository availabilityRepository;
	@Autowired
	protected EquipmentRequestRepository requestRepository;
	@Autowired
	protected RequestStatusTypeRepository statusRepository;
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected UserTypeRepository userTypeRepository;

	private final List<Integer> requestIds = new ArrayList<>();
	private final List<Integer> equipmentIds = new ArrayList<>();
	private final List<Integer> userIds = new ArrayList<>();
	private final List<Integer> categoryIds = new ArrayList<>();
	private final List<Integer> availabilityIds = new ArrayList<>();
	private final List<Integer> statusIds = new ArrayList<>();
	private final List<Integer> userTypeIds = new ArrayList<>();

	protected String unique(String prefix) {
		return prefix + "-" + UUID.randomUUID();
	}

	protected EquipmentCategory newCategory(String name) {
		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName(unique(name));
		category.setDescription("Integration test category");
		category = categoryRepository.saveAndFlush(category);
		categoryIds.add(category.getId());
		return category;
	}

	protected EquipmentAvailabilityType newAvailability(String status) {
		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus(unique(status));
		type.setDescription("Integration test availability");
		type = availabilityRepository.saveAndFlush(type);
		availabilityIds.add(type.getId());
		return type;
	}

	protected EquipmentAvailabilityType newStandardAvailability(String status) {
		var existing = availabilityRepository.findByAvailabilityStatusIgnoreCase(status);
		if (existing.isPresent())
			return existing.get();
		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus(status);
		type.setDescription("Integration test availability");
		type = availabilityRepository.saveAndFlush(type);
		availabilityIds.add(type.getId());
		return type;
	}

	protected RequestStatusType newStatus(String name) {
		var existing = statusRepository.findAll().stream().filter(row -> name.equalsIgnoreCase(row.getStatusType()))
				.findFirst();
		if (existing.isPresent())
			return existing.get();
		RequestStatusType status = new RequestStatusType();
		status.setStatusType(name);
		status.setDescription("Integration test status");
		status = statusRepository.saveAndFlush(status);
		statusIds.add(status.getId());
		return status;
	}

	protected UserType newUserType(String name) {
		UserType type = new UserType();
		type.setUserType(unique(name));
		type = userTypeRepository.saveAndFlush(type);
		userTypeIds.add(type.getId());
		return type;
	}

	protected UserType newStandardUserType(String name) {
		var existing = userTypeRepository.findAll().stream().filter(row -> name.equalsIgnoreCase(row.getUserType()))
				.findFirst();
		if (existing.isPresent())
			return existing.get();
		UserType type = new UserType();
		type.setUserType(name);
		type = userTypeRepository.saveAndFlush(type);
		userTypeIds.add(type.getId());
		return type;
	}

	protected User newUser(UserType type, String emailPrefix) {
		User user = new User();
		user.setFirstName("Integration");
		user.setLastName("Tester");
		user.setEmail(unique(emailPrefix) + "@example.test");
		user.setPassword("not-used-in-auth-test");
		user.setUserType(type);
		user = userRepository.saveAndFlush(user);
		userIds.add(user.getId());
		return user;
	}

	protected Equipment newEquipment(EquipmentCategory category, EquipmentAvailabilityType availability, int quantity) {
		Equipment equipment = new Equipment();
		equipment.setName(unique("equipment"));
		equipment.setCategory(category);
		equipment.setAvailabilityType(availability);
		equipment.setTotalQuantity(quantity);
		equipment.setQuantityLeft(quantity);
		equipment.setDescription("Integration test equipment");
		equipment = equipmentRepository.saveAndFlush(equipment);
		equipmentIds.add(equipment.getId());
		return equipment;
	}

	protected void trackRequest(EquipmentRequest request) {
		requestIds.add(request.getId());
	}

	protected void trackCategory(EquipmentCategory category) {
		categoryIds.add(category.getId());
	}

	protected void trackEquipment(Equipment equipment) {
		equipmentIds.add(equipment.getId());
	}

	protected void trackUser(User user) {
		userIds.add(user.getId());
	}

	@AfterEach
	void cleanIntegrationRows() {
		requestIds.forEach(id -> {
			if (requestRepository.existsById(id))
				requestRepository.deleteById(id);
		});
		equipmentIds.forEach(id -> {
			if (equipmentRepository.existsById(id))
				equipmentRepository.deleteById(id);
		});
		userIds.forEach(id -> {
			if (userRepository.existsById(id))
				userRepository.deleteById(id);
		});
		categoryIds.forEach(id -> {
			if (categoryRepository.existsById(id))
				categoryRepository.deleteById(id);
		});
		availabilityIds.forEach(id -> {
			if (availabilityRepository.existsById(id))
				availabilityRepository.deleteById(id);
		});
		statusIds.forEach(id -> {
			if (statusRepository.existsById(id))
				statusRepository.deleteById(id);
		});
		userTypeIds.forEach(id -> {
			if (userTypeRepository.existsById(id))
				userTypeRepository.deleteById(id);
		});
	}
}
