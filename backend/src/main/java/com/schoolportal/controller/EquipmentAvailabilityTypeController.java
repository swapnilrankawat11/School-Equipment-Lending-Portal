package com.schoolportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.EquipmentAvailabilityTypeDTO;
import com.schoolportal.service.EquipmentAvailabilityTypeService;

@RestController
@RequestMapping("/EquipmentAvailabilityTypesServlet")
public class EquipmentAvailabilityTypeController {

	private final EquipmentAvailabilityTypeService equipmentAvailabilityTypeService;

	public EquipmentAvailabilityTypeController(EquipmentAvailabilityTypeService equipmentAvailabilityTypeService) {
		this.equipmentAvailabilityTypeService = equipmentAvailabilityTypeService;
	}

	@GetMapping
	public ResponseEntity<List<EquipmentAvailabilityTypeDTO>> getAllEquipmentAvailabilityTypes() {

		List<EquipmentAvailabilityTypeDTO> availabilityTypes = equipmentAvailabilityTypeService
				.getAllEquipmentAvailabilityTypes();

		return ResponseEntity.ok(availabilityTypes);
	}
}