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

import com.schoolportal.dto.EquipmentDTO;
import com.schoolportal.service.EquipmentService;

@RestController
@RequestMapping("/EquipmentServlet")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // GET: Retrieve all equipment
    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipments() {

        List<EquipmentDTO> equipments =
                equipmentService.getAllEquipments();

        return ResponseEntity.ok(equipments);
    }

    // POST: Add new equipment
    @PostMapping
    public ResponseEntity<Map<String, Boolean>> addEquipment(
            @RequestBody EquipmentDTO equipmentDTO) {

        boolean result = equipmentService.addEquipment(equipmentDTO);

        return ResponseEntity.ok(Map.of("success", result));
    }

    // PUT: Update existing equipment
    @PutMapping("/{equipmentId}")
    public ResponseEntity<Map<String, Boolean>> updateEquipment(
            @PathVariable int equipmentId,
            @RequestBody EquipmentDTO equipmentDTO) {

        boolean result = equipmentService.updateEquipment(
                equipmentId, equipmentDTO);

        return ResponseEntity.ok(Map.of("success", result));
    }

    // DELETE: Delete equipment
    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<Map<String, Boolean>> deleteEquipment(
            @PathVariable int equipmentId) {

        boolean result = equipmentService.deleteEquipment(equipmentId);

        return ResponseEntity.ok(Map.of("success", result));
    }
}