package com.schoolportal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.schoolportal.dao.UserTypesDAO;
import com.schoolportal.model.UserType;

@WebServlet("/UserTypesServlet")
public class UserTypesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UserTypesDAO dao = new UserTypesDAO();
	private static Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<UserType> userTypes = dao.getUserTypes();
		response.getWriter().write(gson.toJson(userTypes));
	}

}
