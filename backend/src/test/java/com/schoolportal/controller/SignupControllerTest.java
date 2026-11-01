package com.schoolportal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.entity.User;
import com.schoolportal.service.AuthService;

@WebMvcTest(SignupController.class)
@AutoConfigureMockMvc(addFilters = false)
class SignupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AuthService authService;

	@Test
	void signup_shouldReturnSuccessTrue() throws Exception {

		User user = new User();

		when(authService.signup(any(User.class))).thenReturn(true);

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":true}"));

		verify(authService).signup(any(User.class));
	}

	@Test
	void signup_shouldReturnSuccessFalse() throws Exception {

		User user = new User();

		when(authService.signup(any(User.class))).thenReturn(false);

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":false}"));
	}

	@Test
	void signup_shouldReturnBadRequestWhenServiceThrowsException() throws Exception {

		User user = new User();

		when(authService.signup(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
				.andExpect(content().json("{\"error\":\"Email already exists\"}"));
	}
}