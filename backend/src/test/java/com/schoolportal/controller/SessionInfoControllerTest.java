package com.schoolportal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SessionInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessionInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getSessionInfo_shouldReturnSessionDetailsWhenSessionExists() throws Exception {

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", 1);
		session.setAttribute("userTypeId", 2);
		session.setAttribute("userTypeName", "Student");
		session.setAttribute("firstName", "John");
		session.setAttribute("lastName", "Doe");

		mockMvc.perform(get("/SessionInfoServlet").session(session)).andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(1)).andExpect(jsonPath("$.userTypeId").value(2))
				.andExpect(jsonPath("$.userTypeName").value("Student")).andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	void getSessionInfo_shouldReturnUnauthorizedWhenNoSessionExists() throws Exception {

		mockMvc.perform(get("/SessionInfoServlet")).andExpect(status().isUnauthorized())
				.andExpect(content().json("{\"error\":\"No active session\"}"));
	}

	@Test
	void getSessionInfo_shouldReturnUnauthorizedWhenUserIdIsMissing() throws Exception {

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userTypeId", 2);

		mockMvc.perform(get("/SessionInfoServlet").session(session)).andExpect(status().isUnauthorized())
				.andExpect(content().json("{\"error\":\"No active session\"}"));
	}
}