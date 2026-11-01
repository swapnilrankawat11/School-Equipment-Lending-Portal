package com.schoolportal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(LogoutController.class)
@AutoConfigureMockMvc(addFilters = false)
class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void logout_shouldInvalidateExistingSession() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        MvcResult result = mockMvc.perform(post("/LogoutServlet")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"))
                .andReturn();

        org.junit.jupiter.api.Assertions.assertTrue(session.isInvalid());
    }

    @Test
    void logout_shouldReturnSuccessWhenNoSessionExists() throws Exception {

        mockMvc.perform(post("/LogoutServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));
    }
}