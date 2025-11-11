package com.schoolportal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.schoolportal.dao.EquipmentAvailabilityTypesDAO;
import com.schoolportal.model.EquipmentAvailabilityTypes;

@WebServlet("/EquipmentAvailabilityTypesServlet")
public class EquipmentAvailabilityTypesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EquipmentAvailabilityTypesDAO dao = new EquipmentAvailabilityTypesDAO();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<EquipmentAvailabilityTypes> eqat = dao.getAllEquipmentAvailabilityTypes();
		response.getWriter().write(gson.toJson(eqat));
	}

}