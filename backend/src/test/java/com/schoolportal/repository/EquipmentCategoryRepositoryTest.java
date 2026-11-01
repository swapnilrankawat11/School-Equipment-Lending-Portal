package com.schoolportal.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.EquipmentCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentCategoryRepositoryTest {

	@Autowired
	private EquipmentCategoryRepository repository;

	@Test
	void save_shouldPersistCategory() {

		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName("Electronics");
		category.setDescription("Electronic equipment");

		EquipmentCategory saved = repository.save(category);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getCategoryName()).isEqualTo("Electronics");
		assertThat(saved.getDescription()).isEqualTo("Electronic equipment");
	}

	@Test
	void findById_shouldReturnCategory() {

		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName("Furniture");

		EquipmentCategory saved = repository.save(category);

		Optional<EquipmentCategory> result = repository.findById(saved.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getCategoryName()).isEqualTo("Furniture");
	}

	@Test
	void findById_shouldReturnEmptyWhenCategoryDoesNotExist() {

		assertThat(repository.findById(999)).isEmpty();
	}

	@Test
	void findAll_shouldReturnAllCategories() {

		EquipmentCategory first = new EquipmentCategory();
		first.setCategoryName("Electronics");

		EquipmentCategory second = new EquipmentCategory();
		second.setCategoryName("Furniture");

		repository.saveAll(List.of(first, second));

		assertThat(repository.findAll()).hasSize(2);
	}

	@Test
	void update_shouldModifyCategory() {

		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName("Electronics");

		EquipmentCategory saved = repository.save(category);

		saved.setCategoryName("Computer Equipment");
		repository.save(saved);

		Optional<EquipmentCategory> result = repository.findById(saved.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getCategoryName()).isEqualTo("Computer Equipment");
	}

	@Test
	void delete_shouldRemoveCategory() {

		EquipmentCategory category = new EquipmentCategory();
		category.setCategoryName("Electronics");

		EquipmentCategory saved = repository.save(category);

		repository.deleteById(saved.getId());

		assertThat(repository.findById(saved.getId())).isEmpty();
	}
}