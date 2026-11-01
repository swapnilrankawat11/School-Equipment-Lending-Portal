package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class EquipmentAvailabilityTypeIntegrationTest extends AbstractIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void getAvailabilityTypesIncludesPersistedDatabaseRecord() throws Exception {
        var created = newAvailability("integration-availability");
        var result = mockMvc.perform(get("/EquipmentAvailabilityTypesServlet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + created.getId() + ")]" ).exists())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        org.junit.jupiter.api.Assertions.assertTrue(body.contains(created.getAvailabilityStatus()));
        assertEquals(created.getDescription(), availabilityRepository.findById(created.getId()).orElseThrow().getDescription());
    }
}
