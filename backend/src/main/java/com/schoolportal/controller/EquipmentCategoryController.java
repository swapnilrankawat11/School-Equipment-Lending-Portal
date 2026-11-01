package com.schoolportal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.EquipmentCategoryDTO;
import com.schoolportal.service.EquipmentCategoryService;

@RestController
@RequestMapping("/EquipmentCategoryServlet")
public class EquipmentCategoryController {

	private final EquipmentCategoryService equipmentCategoryService;

	public EquipmentCategoryController(EquipmentCategoryService equipmentCategoryService) {
		this.equipmentCategoryService = equipmentCategoryService;
	}

	// GET: Retrieve all categories
	@GetMapping
	public ResponseEntity<List<EquipmentCategoryDTO>> getAllEquipmentCategories() {

		List<EquipmentCategoryDTO> categories = equipmentCategoryService.getAllEquipmentCategories();

		return ResponseEntity.ok(categories);
	}

	// POST: Add a new category
	@PostMapping
	public ResponseEntity<Map<String, Boolean>> addEquipmentCategory(@RequestBody EquipmentCategoryDTO categoryDTO) {

		boolean result = equipmentCategoryService.addEquipmentCategory(categoryDTO);

		return ResponseEntity.ok(Map.of("success", result));
	}

	// PUT: Update an existing category
	@PutMapping("/{categoryId}")
	public ResponseEntity<Map<String, Boolean>> updateEquipmentCategory(@PathVariable int categoryId,
			@RequestBody EquipmentCategoryDTO categoryDTO) {

		boolean result = equipmentCategoryService.updateEquipmentCategory(categoryId, categoryDTO);

		return ResponseEntity.ok(Map.of("success", result));
	}

	// DELETE: Delete a category
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Map<String, Boolean>> deleteEquipmentCategory(@PathVariable int categoryId) {

		boolean result = equipmentCategoryService.deleteEquipmentCategory(categoryId);

		return ResponseEntity.ok(Map.of("success", result));
	}
}