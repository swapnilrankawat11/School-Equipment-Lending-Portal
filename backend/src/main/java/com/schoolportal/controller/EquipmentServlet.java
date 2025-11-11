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
import com.schoolportal.dao.EquipmentDAO;
import com.schoolportal.model.Equipment;
import com.schoolportal.service.EquipmentService;

@WebServlet("/EquipmentServlet/*")
public class EquipmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EquipmentDAO dao = new EquipmentDAO();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<Equipment> equipments = dao.getAllEquipments();
		response.getWriter().write(gson.toJson(equipments));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Equipment eq = gson.fromJson(request.getReader(), Equipment.class);
		boolean result = dao.addEquipment(eq);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		EquipmentService service = new EquipmentService();

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(gson.toJson(Map.of("error", "Equipment ID is required in URL path")));
				return;
			}

			int eqId = Integer.parseInt(pathInfo.substring(1));
			Equipment eq = gson.fromJson(request.getReader(), Equipment.class);

			boolean result = service.updateEquipment(eqId, eq);
			response.getWriter().write(gson.toJson(Map.of("success", result)));

		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Map.of("error", "Invalid equipment ID format")));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		int eqId = Integer.parseInt(pathInfo.substring(1));
		boolean result = dao.deleteEquipment(eqId);
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}

}
