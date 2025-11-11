package com.schoolportal.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.schoolportal.model.User;
import com.schoolportal.service.AuthService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			User loginData = gson.fromJson(request.getReader(), User.class);
			AuthService service = new AuthService();
			User user = service.login(loginData.getEmail(), loginData.getPassword());

			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userTypeId", user.getUserTypeId());
			session.setAttribute("userTypeName", user.getUserTypeName());
			session.setAttribute("firstName", user.getFirstName());
			session.setAttribute("lastName", user.getLastName());
			session.setMaxInactiveInterval(60 * 60); // 1 hour

			response.getWriter().write(gson.toJson(Map.of("success", true, "user", user)));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}
}
