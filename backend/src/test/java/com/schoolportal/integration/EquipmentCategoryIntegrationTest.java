package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.schoolportal.dto.EquipmentCategoryDTO;
import com.schoolportal.entity.EquipmentCategory;

class EquipmentCategoryIntegrationTest extends AbstractIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void categoryCrudPersistsAndRemovesRecord() throws Exception {
        mockMvc.perform(get("/EquipmentCategoryServlet"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray());

        String categoryName = unique("Integration Category");
        EquipmentCategoryDTO create = dto(categoryName, "Created description");
        mockMvc.perform(post("/EquipmentCategoryServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

        EquipmentCategory created = categoryRepository.findAll().stream()
                .filter(row -> categoryName.equals(row.getCategoryName())).findFirst().orElseThrow();
        trackCategory(created);

        JsonNode rows = objectMapper.readTree(mockMvc.perform(get("/EquipmentCategoryServlet"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        int id = -1;
        for (JsonNode row : rows) if (categoryName.equals(row.path("categoryName").asText())) id = row.path("id").asInt();
        org.junit.jupiter.api.Assertions.assertTrue(id > 0);
        assertEquals("Created description", categoryRepository.findById(id).orElseThrow().getDescription());

        mockMvc.perform(put("/EquipmentCategoryServlet/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto("Renamed Integration Category", "Updated description"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        assertEquals("Renamed Integration Category", categoryRepository.findById(id).orElseThrow().getCategoryName());

        mockMvc.perform(delete("/EquipmentCategoryServlet/{id}", id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        assertFalse(categoryRepository.existsById(id));
    }

    private EquipmentCategoryDTO dto(String name, String description) {
        EquipmentCategoryDTO dto = new EquipmentCategoryDTO();
        dto.setCategoryName(name);
        dto.setDescription(description);
        return dto;
    }
}
