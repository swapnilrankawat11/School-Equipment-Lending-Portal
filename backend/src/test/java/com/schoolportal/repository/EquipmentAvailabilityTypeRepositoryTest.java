package com.schoolportal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.EquipmentAvailabilityType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentAvailabilityTypeRepositoryTest {

	@Autowired
	private EquipmentAvailabilityTypeRepository repository;

	@Test
	void save_shouldPersistAvailabilityType() {

		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus("Available");
		type.setDescription("Equipment is available");

		EquipmentAvailabilityType saved = repository.save(type);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getAvailabilityStatus()).isEqualTo("Available");
		assertThat(saved.getDescription()).isEqualTo("Equipment is available");
	}

	@Test
	void findById_shouldReturnAvailabilityType() {

		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus("Unavailable");

		EquipmentAvailabilityType saved = repository.save(type);

		Optional<EquipmentAvailabilityType> result = repository.findById(saved.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getAvailabilityStatus()).isEqualTo("Unavailable");
	}

	@Test
	void findByAvailabilityStatusIgnoreCase_shouldFindMatchingStatus() {

		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus("Available");

		repository.save(type);

		Optional<EquipmentAvailabilityType> result = repository.findByAvailabilityStatusIgnoreCase("available");

		assertThat(result).isPresent();
		assertThat(result.get().getAvailabilityStatus()).isEqualTo("Available");
	}

	@Test
	void findByAvailabilityStatusIgnoreCase_shouldReturnEmptyWhenNotFound() {

		Optional<EquipmentAvailabilityType> result = repository.findByAvailabilityStatusIgnoreCase("Unknown");

		assertThat(result).isEmpty();
	}

	@Test
	void findAll_shouldReturnAllAvailabilityTypes() {

		EquipmentAvailabilityType first = new EquipmentAvailabilityType();
		first.setAvailabilityStatus("Available");

		EquipmentAvailabilityType second = new EquipmentAvailabilityType();
		second.setAvailabilityStatus("Unavailable");

		repository.save(first);
		repository.save(second);

		assertThat(repository.findAll()).hasSize(2);
	}

	@Test
	void delete_shouldRemoveAvailabilityType() {

		EquipmentAvailabilityType type = new EquipmentAvailabilityType();
		type.setAvailabilityStatus("Available");

		EquipmentAvailabilityType saved = repository.save(type);

		repository.deleteById(saved.getId());

		assertThat(repository.findById(saved.getId())).isEmpty();
	}
}