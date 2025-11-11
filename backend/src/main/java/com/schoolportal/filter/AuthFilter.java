package com.schoolportal.filter;

import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter(urlPatterns = { "/school-equipment-lending-portal/api/*", "/school-equipment-lending-portal/admin/*",
		"/school-equipment-lending-portal/staff/*", "/school-equipment-lending-portal/student/*" })

public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		Gson gson = new Gson();

		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("userId") == null) {
			denyAccess(res, gson, "Unauthorized! Please login.");
			return;
		}

		String userTypeName = (String) session.getAttribute("userTypeName");
		String path = req.getRequestURI();
		if ((path.contains("/admin") && !userTypeName.equalsIgnoreCase("admin"))
				|| (path.contains("/staff") && !userTypeName.equalsIgnoreCase("staff"))
				|| (path.contains("/student") && !userTypeName.equalsIgnoreCase("student"))) {
			denyAccess(res, gson, "Access denied! You don't have permission to access this page.");
			return;
		}

		chain.doFilter(request, response);
	}

	private void denyAccess(HttpServletResponse res, Gson gson, String message) throws IOException {
		res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		res.setContentType("application/json");
		res.getWriter().write(gson.toJson(Map.of("error", message)));
	}
}
