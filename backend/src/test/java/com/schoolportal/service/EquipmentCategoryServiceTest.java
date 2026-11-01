
package com.schoolportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolportal.dto.EquipmentCategoryDTO;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.repository.EquipmentCategoryRepository;

@ExtendWith(MockitoExtension.class)
class EquipmentCategoryServiceTest {

	@Mock
	private EquipmentCategoryRepository repository;

	@InjectMocks
	private EquipmentCategoryService categoryService;

	private EquipmentCategory category;

	@BeforeEach
	void setUp() {

		category = new EquipmentCategory();

		category.setId(1);
		category.setCategoryName("Electronics");
		category.setDescription("Electronic equipment");
		category.setCreatedBy(10);
		category.setModifiedBy(10);
	}

	private EquipmentCategoryDTO createDTO() {

		EquipmentCategoryDTO dto = new EquipmentCategoryDTO();

		dto.setCategoryName("Furniture");
		dto.setDescription("Classroom furniture");
		dto.setCreatedBy(10);
		dto.setModifiedBy(20);

		return dto;
	}

	// GET ALL CATEGORIES

	@Test
	void getAllEquipmentCategories_shouldReturnMappedDTOs() {

		when(repository.findAll()).thenReturn(List.of(category));

		List<EquipmentCategoryDTO> result = categoryService.getAllEquipmentCategories();

		assertEquals(1, result.size());

		EquipmentCategoryDTO dto = result.get(0);

		assertEquals(1, dto.getId());
		assertEquals("Electronics", dto.getCategoryName());
		assertEquals("Electronic equipment", dto.getDescription());
		assertEquals(10, dto.getCreatedBy());
		assertEquals(10, dto.getModifiedBy());
	}

	@Test
	void getAllEquipmentCategories_emptyList_shouldReturnEmptyList() {

		when(repository.findAll()).thenReturn(List.of());

		List<EquipmentCategoryDTO> result = categoryService.getAllEquipmentCategories();

		assertTrue(result.isEmpty());
	}

	// ADD CATEGORY

	@Test
	void addEquipmentCategory_validDTO_shouldSaveCategory() {

		EquipmentCategoryDTO dto = createDTO();

		when(repository.save(any(EquipmentCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

		assertTrue(categoryService.addEquipmentCategory(dto));

		ArgumentCaptor<EquipmentCategory> captor = ArgumentCaptor.forClass(EquipmentCategory.class);

		verify(repository).save(captor.capture());

		EquipmentCategory saved = captor.getValue();

		assertEquals("Furniture", saved.getCategoryName());
		assertEquals("Classroom furniture", saved.getDescription());
		assertEquals(10, saved.getCreatedBy());
		assertEquals(20, saved.getModifiedBy());
	}

	// UPDATE CATEGORY

	@Test
	void updateEquipmentCategory_existingCategory_shouldUpdateAndReturnTrue() {

		EquipmentCategoryDTO dto = createDTO();

		when(repository.findById(1)).thenReturn(Optional.of(category));

		assertTrue(categoryService.updateEquipmentCategory(1, dto));

		assertEquals("Furniture", category.getCategoryName());
		assertEquals("Classroom furniture", category.getDescription());
		assertEquals(20, category.getModifiedBy());

		verify(repository).save(category);
	}

	@Test
	void updateEquipmentCategory_missingCategory_shouldReturnFalse() {

		when(repository.findById(99)).thenReturn(Optional.empty());

		assertFalse(categoryService.updateEquipmentCategory(99, createDTO()));

		verify(repository, never()).save(any());
	}

	// DELETE CATEGORY

	@Test
	void deleteEquipmentCategory_existingCategory_shouldDeleteAndReturnTrue() {

		when(repository.existsById(1)).thenReturn(true);

		assertTrue(categoryService.deleteEquipmentCategory(1));

		verify(repository).deleteById(1);
	}

	@Test
	void deleteEquipmentCategory_missingCategory_shouldReturnFalse() {

		when(repository.existsById(99)).thenReturn(false);

		assertFalse(categoryService.deleteEquipmentCategory(99));

		verify(repository, never()).deleteById(anyInt());
	}
}
