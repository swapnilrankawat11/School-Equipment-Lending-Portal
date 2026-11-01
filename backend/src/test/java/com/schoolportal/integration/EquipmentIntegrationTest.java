package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.dto.EquipmentDTO;
import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;

class EquipmentIntegrationTest extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void equipmentCrudPersistsFieldsAndRelationships() throws Exception {
        EquipmentCategory category = newCategory("hardware");
        EquipmentAvailabilityType available = newStandardAvailability("Available");

        mockMvc.perform(get("/EquipmentServlet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        EquipmentDTO create = equipmentDto("Integration Laptop", category, available, 8, 8);
        mockMvc.perform(post("/EquipmentServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Equipment persistedRow = equipmentRepository.findAllWithDetails().stream()
                .filter(row -> "Integration Laptop".equals(row.getName())).findFirst().orElseThrow();
        trackEquipment(persistedRow);

        JsonNode created = objectMapper.readTree(mockMvc.perform(get("/EquipmentServlet"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        JsonNode persisted = null;
        for (JsonNode item : created) {
            if ("Integration Laptop".equals(item.path("name").asText())) persisted = item;
        }
        assertNotNull(persisted);
        int id = persisted.path("id").asInt();
        Equipment row = equipmentRepository.findById(id).orElseThrow();
        assertEquals(category.getId(), row.getCategory().getId());
        assertEquals(available.getId(), row.getAvailabilityType().getId());
        assertEquals(8, row.getQuantityLeft());

        EquipmentDTO update = equipmentDto("Updated Integration Laptop", category, available, 12, 12);
        mockMvc.perform(put("/EquipmentServlet/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        Equipment updated = equipmentRepository.findById(id).orElseThrow();
        assertEquals("Updated Integration Laptop", updated.getName());
        assertEquals(12, updated.getTotalQuantity());
        assertEquals(12, updated.getQuantityLeft());

        mockMvc.perform(delete("/EquipmentServlet/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        assertFalse(equipmentRepository.existsById(id));
    }

    @Test
    void deleteOfNonexistentEquipmentReturnsFalse() throws Exception {
        int missingId = Integer.MAX_VALUE;
        while (equipmentRepository.existsById(missingId)) missingId--;
        mockMvc.perform(delete("/EquipmentServlet/{id}", missingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    private EquipmentDTO equipmentDto(String name, EquipmentCategory category,
            EquipmentAvailabilityType availability, int total, int left) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName(name);
        dto.setCategoryId(category.getId());
        dto.setAvailabilityId(availability.getId());
        dto.setTotalQuantity(total);
        dto.setQuantityLeft(left);
        dto.setDescription("Integration workflow");
        return dto;
    }
}
