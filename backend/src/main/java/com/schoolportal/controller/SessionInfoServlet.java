package com.schoolportal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.schoolportal.model.SessionInfo;

@WebServlet("/SessionInfoServlet")
public class SessionInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		response.setContentType("application/json");

		if (session != null && session.getAttribute("userId") != null) {
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setUserId((Integer) session.getAttribute("userId"));
			sessionInfo.setUserTypeId((Integer) session.getAttribute("userTypeId"));
			sessionInfo.setUserTypeName((String) session.getAttribute("userTypeName"));
			sessionInfo.setFirstName((String) session.getAttribute("firstName"));
			sessionInfo.setLastName((String) session.getAttribute("lastName"));
			response.getWriter().write(gson.toJson(sessionInfo));
		} else {
			response.getWriter().write(gson.toJson(Map.of("error", "No active session")));
		}

	}

}
