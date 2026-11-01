package com.schoolportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.EquipmentCategoryDTO;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.repository.EquipmentCategoryRepository;

@Service
@Transactional
public class EquipmentCategoryService {

	private final EquipmentCategoryRepository repository;

	public EquipmentCategoryService(EquipmentCategoryRepository repository) {
		this.repository = repository;
	}

	// GET: Retrieve all categories
	@Transactional(readOnly = true)
	public List<EquipmentCategoryDTO> getAllEquipmentCategories() {

		List<EquipmentCategory> categories = repository.findAll();

		return categories.stream().map(category -> {

			EquipmentCategoryDTO dto = new EquipmentCategoryDTO();

			dto.setId(category.getId());
			dto.setCreatedBy(category.getCreatedBy());
			dto.setModifiedBy(category.getModifiedBy());
			dto.setCategoryName(category.getCategoryName());
			dto.setDescription(category.getDescription());
			dto.setCreatedAt(category.getCreatedAt());
			dto.setModifiedAt(category.getModifiedAt());

			return dto;

		}).toList();
	}

	// POST: Add a new category
	public boolean addEquipmentCategory(EquipmentCategoryDTO dto) {

		EquipmentCategory category = new EquipmentCategory();

		category.setCategoryName(dto.getCategoryName());
		category.setDescription(dto.getDescription());
		category.setCreatedBy(dto.getCreatedBy());
		category.setModifiedBy(dto.getModifiedBy());

		repository.save(category);

		return true;
	}

	// PUT: Update an existing category
	public boolean updateEquipmentCategory(int categoryId, EquipmentCategoryDTO dto) {

		EquipmentCategory category = repository.findById(categoryId).orElse(null);

		if (category == null) {
			return false;
		}

		category.setCategoryName(dto.getCategoryName());
		category.setDescription(dto.getDescription());
		category.setModifiedBy(dto.getModifiedBy());

		repository.save(category);

		return true;
	}

	// DELETE: Delete a category
	public boolean deleteEquipmentCategory(int categoryId) {

		if (!repository.existsById(categoryId)) {
			return false;
		}

		repository.deleteById(categoryId);

		return true;
	}
}