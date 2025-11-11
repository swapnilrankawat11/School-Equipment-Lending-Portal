package com.schoolportal.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CORSFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		String origin = req.getHeader("Origin");

		if ("http://localhost:3000".equals(origin)) {
			res.setHeader("Access-Control-Allow-Origin", origin);
		}

		res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		res.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		chain.doFilter(request, response);
	}
}
