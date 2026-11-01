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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.EquipmentRequestDTO;
import com.schoolportal.service.EquipmentRequestService;

@RestController
@RequestMapping("/EquipmentRequestServlet")
public class EquipmentRequestController {

	private final EquipmentRequestService equipmentRequestService;

	public EquipmentRequestController(EquipmentRequestService equipmentRequestService) {
		this.equipmentRequestService = equipmentRequestService;
	}

	// POST: Create a new equipment request
	@PostMapping
	public ResponseEntity<Map<String, Boolean>> createEquipmentRequest(@RequestBody EquipmentRequestDTO requestDTO) {

		boolean result = equipmentRequestService.createEquipmentRequest(requestDTO);

		return ResponseEntity.ok(Map.of("success", result));
	}

	// GET: Retrieve requests by status OR by student and status
	@GetMapping("/{id}")
	public ResponseEntity<List<EquipmentRequestDTO>> getEquipmentRequests(@PathVariable int id,
			@RequestParam(required = false) Integer statusFilterId) {

		List<EquipmentRequestDTO> requests;

		if (statusFilterId != null) {
			requests = equipmentRequestService.getEquipmentRequestsByStudentAndStatus(id, statusFilterId);
		} else {
			requests = equipmentRequestService.getEquipmentRequestsByStatusType(id);
		}

		return ResponseEntity.ok(requests);
	}

	// PUT: Update an existing equipment request
	@PutMapping("/{requestId}")
	public ResponseEntity<Map<String, Boolean>> updateEquipmentRequest(@PathVariable int requestId,
			@RequestBody EquipmentRequestDTO requestDTO) {

		boolean result = equipmentRequestService.updateEquipmentRequest(requestId, requestDTO);

		return ResponseEntity.ok(Map.of("success", result));
	}

	// DELETE: Delete an equipment request
	@DeleteMapping("/{requestId}")
	public ResponseEntity<Map<String, Boolean>> deleteEquipmentRequest(@PathVariable int requestId) {

		boolean result = equipmentRequestService.deleteEquipmentRequest(requestId);

		return ResponseEntity.ok(Map.of("success", result));
	}
}