package com.schoolportal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.schoolportal.dao.RequestStatusTypesDAO;
import com.schoolportal.model.RequestStatusTypes;

@WebServlet("/RequestStatusTypesServlet")
public class RequestStatusTypesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static RequestStatusTypesDAO dao = new RequestStatusTypesDAO();
	private static Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<RequestStatusTypes> list = dao.getAllRequestStatusTypes();
		response.getWriter().write(gson.toJson(list));
	}

}
