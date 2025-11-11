package com.schoolportal.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.schoolportal.model.User;
import com.schoolportal.service.AuthService;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			User user = gson.fromJson(request.getReader(), User.class);
			AuthService service = new AuthService();
			boolean isAccountCreated = service.signup(user);
			response.getWriter().write(gson.toJson(Map.of("success", isAccountCreated)));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}
}
