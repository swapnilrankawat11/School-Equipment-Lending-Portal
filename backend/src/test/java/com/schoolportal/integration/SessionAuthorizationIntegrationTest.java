package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

class SessionAuthorizationIntegrationTest extends AbstractIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void actualLegacyServletEndpointIsReachableWithoutSession() throws Exception {
        mockMvc.perform(get("/EquipmentServlet"))
                .andExpect(status().isOk());
    }

    @Test
    void corsPreflightForActualLoginEndpointAllowsConfiguredFrontend() throws Exception {
        var response = mockMvc.perform(options("/LoginServlet")
                        .header(HttpHeaders.ORIGIN, "http://localhost:3000")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type"))
                .andExpect(status().isOk()).andReturn().getResponse();
        assertEquals("http://localhost:3000", response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
        assertEquals("true", response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    @Test
    void corsPreflightRejectsAnUnconfiguredFrontendOrigin() throws Exception {
        var response = mockMvc.perform(options("/LoginServlet")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
                .andExpect(status().isForbidden()).andReturn().getResponse();
        assertNull(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
