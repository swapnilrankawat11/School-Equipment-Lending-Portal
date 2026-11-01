package com.schoolportal.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

class SessionAuthorizationFilterTest {

	private SessionAuthorizationFilter filter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockFilterChain filterChain;

	@BeforeEach
	void setUp() {
		filter = new SessionAuthorizationFilter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
	}

	private void performFilter(String method, String path) throws ServletException, IOException {
		request.setMethod(method);
		request.setServletPath(path);
		filter.doFilter(request, response, filterChain);
	}

	private void createSession(String role) {
		HttpSession session = request.getSession(true);
		session.setAttribute("userId", 1);
		session.setAttribute("userTypeName", role);
	}

	@Test
	void apiPath_withoutSession_shouldReturnForbidden() throws Exception {
		performFilter("GET", "/api/equipment");

		assertEquals(403, response.getStatus());
		assertTrue(response.getContentAsString().contains("Unauthorized"));
	}

	@Test
	void apiPath_withLoggedInUser_shouldContinue() throws Exception {
		createSession("Student");

		performFilter("GET", "/api/equipment");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void adminPath_withAdminRole_shouldContinue() throws Exception {
		createSession("Admin");

		performFilter("GET", "/admin/dashboard");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void adminPath_withStudentRole_shouldReturnForbidden() throws Exception {
		createSession("Student");

		performFilter("GET", "/admin/dashboard");

		assertEquals(403, response.getStatus());
		assertTrue(response.getContentAsString().contains("Access denied"));
	}

	@Test
	void staffPath_withStaffRole_shouldContinue() throws Exception {
		createSession("Staff");

		performFilter("GET", "/staff/dashboard");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void staffPath_withAdminRole_shouldReturnForbidden() throws Exception {
		createSession("Admin");

		performFilter("GET", "/staff/dashboard");

		assertEquals(403, response.getStatus());
	}

	@Test
	void studentPath_withStudentRole_shouldContinue() throws Exception {
		createSession("Student");

		performFilter("GET", "/student/dashboard");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void studentPath_withStaffRole_shouldReturnForbidden() throws Exception {
		createSession("Staff");

		performFilter("GET", "/student/dashboard");

		assertEquals(403, response.getStatus());
	}

	@Test
	void protectedPath_withSessionButNoRole_shouldReturnForbidden() throws Exception {
		HttpSession session = request.getSession(true);
		session.setAttribute("userId", 1);

		performFilter("GET", "/admin/dashboard");

		assertEquals(403, response.getStatus());
	}

	@Test
	void optionsRequest_withoutSession_shouldContinue() throws Exception {
		performFilter("OPTIONS", "/admin/dashboard");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void unprotectedPath_withoutSession_shouldContinue() throws Exception {
		performFilter("GET", "/EquipmentRequestServlet");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}

	@Test
	void apiPath_withUnknownRole_shouldContinue() throws Exception {
		createSession("UnknownRole");

		performFilter("GET", "/api/equipment");

		assertEquals(200, response.getStatus());
		assertTrue(filterChain.getRequest() != null);
	}
}