
package com.schoolportal.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

public class SessionAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (request.getMethod().equalsIgnoreCase("OPTIONS") || !isProtectedPath(path)) {

			filterChain.doFilter(request, response);
			return;
		}

		HttpSession session = request.getSession(false);

		// Check whether the user is logged in.
		if (session == null || session.getAttribute("userId") == null) {

			denyAccess(response, "Unauthorized! Please login.");

			return;
		}

		String userTypeName = (String) session.getAttribute("userTypeName");

		// Check role-based permissions.
		if (!hasRequiredRole(path, userTypeName)) {

			denyAccess(response, "Access denied! You don't have permission to access this page.");

			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isProtectedPath(String path) {

		return path.startsWith("/api/") || path.startsWith("/admin/") || path.startsWith("/staff/")
				|| path.startsWith("/student/");
	}

	private boolean hasRequiredRole(String path, String userTypeName) {

		if (userTypeName == null) {
			return false;
		}

		if (path.startsWith("/admin/")) {
			return userTypeName.equalsIgnoreCase("admin");
		}

		if (path.startsWith("/staff/")) {
			return userTypeName.equalsIgnoreCase("staff");
		}

		if (path.startsWith("/student/")) {
			return userTypeName.equalsIgnoreCase("student");
		}

		// /api/* requires login but has no additional role restriction.
		return true;
	}

	private void denyAccess(HttpServletResponse response, String message) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write("{\"error\":\"" + message + "\"}");
	}
}
