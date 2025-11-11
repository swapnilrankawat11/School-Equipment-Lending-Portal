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
import com.schoolportal.dao.EquipmentRequestDAO;
import com.schoolportal.model.EquipmentRequest;
import com.schoolportal.service.EquipmentRequestService;

@WebServlet("/EquipmentRequestServlet/*")
public class EquipmentRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static EquipmentRequestDAO dao = new EquipmentRequestDAO();
	private static Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		EquipmentRequest eqReq = gson.fromJson(request.getReader(), EquipmentRequest.class);
		boolean result = dao.createEquipmentRequest(eqReq);
		response.getWriter().write(gson.toJson(Map.of("success", result)));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		try {
			String pathInfo = request.getPathInfo(); // e.g., /1
			if (pathInfo == null || pathInfo.equals("/")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(gson.toJson(Map.of("error", "ID is required in URL path")));
				return;
			}

			String[] parts = pathInfo.split("/");

			if (parts.length == 2) {
				int id = Integer.parseInt(parts[1]);
				String statusFilterParam = request.getParameter("statusFilterId");

				List<EquipmentRequest> list;

				if (statusFilterParam != null) {
					int statusFilterId = Integer.parseInt(statusFilterParam);
					list = dao.getEquipmentRequestsByStudentAndStatus(id, statusFilterId);
				} else {
					list = dao.getEquipmentRequestsByStatusType(id);
				}

				response.getWriter().write(gson.toJson(list));
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(gson.toJson(Map.of("error", "Invalid path")));
			}

		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Map.of("error", "Invalid ID format")));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		EquipmentRequestService service = new EquipmentRequestService();
		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(gson.toJson(Map.of("error", "Request ID is required")));
				return;
			}
			int reqId = Integer.parseInt(pathInfo.substring(1));
			EquipmentRequest eqReq = gson.fromJson(request.getReader(), EquipmentRequest.class);
			boolean result = service.updateEquipmentRequest(reqId, eqReq);
			response.getWriter().write(gson.toJson(Map.of("success", result)));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(gson.toJson(Map.of("error", "Request ID is required")));
				return;
			}
			int requestId = Integer.parseInt(pathInfo.substring(1));
			boolean result = dao.deleteEquipmentRequest(requestId);
			response.getWriter().write(gson.toJson(Map.of("success", result)));
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Map.of("error", "Invalid request ID format")));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
		}
	}
}
