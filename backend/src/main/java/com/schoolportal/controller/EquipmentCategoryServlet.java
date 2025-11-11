package com.schoolportal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.schoolportal.dao.EquipmentCategoryDAO;
import com.schoolportal.model.EquipmentCategory;

@WebServlet("/EquipmentCategoryServlet/*")
public class EquipmentCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EquipmentCategoryDAO dao = new EquipmentCategoryDAO();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<EquipmentCategory> eqCategories = dao.getAllEquipmentCategories();
		response.getWriter().write(gson.toJson(eqCategories));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EquipmentCategory category = gson.fromJson(request.getReader(), EquipmentCategory.class);
		boolean result = dao.addEquipmentCategory(category);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		int categoryId = Integer.parseInt(pathInfo.substring(1));
		EquipmentCategory category = gson.fromJson(request.getReader(), EquipmentCategory.class);
		boolean result = dao.updateEquipmentCategory(categoryId, category);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		int categoryId = Integer.parseInt(pathInfo.substring(1));
		boolean result = dao.deleteEquipmentCategory(categoryId);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}
}
