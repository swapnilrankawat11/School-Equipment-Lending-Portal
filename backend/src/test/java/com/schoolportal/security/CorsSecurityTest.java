package com.schoolportal.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CorsSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void preflightRequest_fromAllowedOrigin_shouldReturnCorsHeaders() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(options("/LoginServlet").header(HttpHeaders.ORIGIN, "http://localhost:3000")
						.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
						.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type,Authorization"))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertEquals("http://localhost:3000", response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
		assertEquals("true", response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
		assertNotNull(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
		assertNotNull(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
		assertNotNull(response.getHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE));
		assertEquals("3600", response.getHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE));
	}

	@Test
	void preflightRequest_fromDisallowedOrigin_shouldNotReturnAllowOriginHeader() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(options("/LoginServlet").header(HttpHeaders.ORIGIN, "http://localhost:5173")
						.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
				.andExpect(status().isForbidden()).andReturn().getResponse();

		assertEquals(null, response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
	}

	@Test
	void preflightRequest_withAllowedMethod_shouldReturnPostInAllowedMethods() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(options("/LoginServlet").header(HttpHeaders.ORIGIN, "http://localhost:3000")
						.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertTrue(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS).contains("POST"));
	}

	@Test
	void preflightRequest_withDisallowedMethod_shouldBeRejected() throws Exception {
		mockMvc.perform(options("/LoginServlet").header(HttpHeaders.ORIGIN, "http://localhost:3000")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PATCH")).andExpect(status().isForbidden());
	}

	@Test
	void preflightRequest_withDisallowedHeader_shouldBeRejected() throws Exception {
		mockMvc.perform(options("/LoginServlet").header(HttpHeaders.ORIGIN, "http://localhost:3000")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "X-Custom-Header"))
				.andExpect(status().isForbidden());
	}
}