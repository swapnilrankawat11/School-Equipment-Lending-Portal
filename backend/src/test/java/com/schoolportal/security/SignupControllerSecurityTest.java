package com.schoolportal.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.entity.User;
import com.schoolportal.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@Test
	void signup_withValidUser_shouldReturnSuccess() throws Exception {
		when(authService.signup(any(User.class))).thenReturn(true);

		User user = new User();
		user.setFirstName("Test");
		user.setLastName("Student");
		user.setEmail("signup@example.com");
		user.setPassword("password123");

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	void signup_whenServiceReturnsFalse_shouldReturnSuccessFalse() throws Exception {
		when(authService.signup(any(User.class))).thenReturn(false);

		User user = new User();
		user.setFirstName("Test");
		user.setLastName("Student");
		user.setEmail("signup-false@example.com");
		user.setPassword("password123");

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void signup_whenServiceThrowsException_shouldReturnBadRequest() throws Exception {
		when(authService.signup(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

		User user = new User();
		user.setFirstName("Test");
		user.setLastName("Student");
		user.setEmail("duplicate@example.com");
		user.setPassword("password123");

		mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Email already exists"));
	}
}