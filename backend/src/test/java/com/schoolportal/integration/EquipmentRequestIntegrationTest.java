package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.dto.EquipmentRequestDTO;
import com.schoolportal.entity.EquipmentRequest;
import com.schoolportal.entity.EquipmentAvailabilityType;

class EquipmentRequestIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void requestCreationRetrievalAndApprovalUpdatePersistAcrossLayers() throws Exception {
		var category = newCategory("request-category");
		EquipmentAvailabilityType available = newStandardAvailability("Available");
		EquipmentAvailabilityType lentOut = newStandardAvailability("Lent Out");
		var equipment = newEquipment(category, available, 6);
		var studentType = newUserType("Student");
		var student = newUser(studentType, "request-user");
		var pending = newStatus("Pending");
		var approved = newStatus("Approved");

		EquipmentRequestDTO create = new EquipmentRequestDTO();
		create.setEquipmentId(equipment.getId());
		create.setQuantityRequested(2);
		create.setPurpose("Science fair project");
		create.setCreatedBy(student.getId());
		mockMvc.perform(post("/EquipmentRequestServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(create))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));

		EquipmentRequest request = requestRepository.findAll().stream()
				.filter(row -> student.getId().equals(row.getCreatedBy()))
				.filter(row -> "Science fair project".equals(row.getPurpose())).findFirst().orElseThrow();
		trackRequest(request);
		assertEquals(equipment.getId(), request.getEquipment().getId());
		assertEquals(pending.getId(), request.getRequestStatus().getId());
		assertEquals(2, request.getQuantityRequested());

		mockMvc.perform(get("/EquipmentRequestServlet/{statusId}", pending.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(request.getId()))
				.andExpect(jsonPath("$[0].requestStatusId").value(pending.getId()));

		mockMvc.perform(get("/EquipmentRequestServlet/{studentId}", student.getId()).param("statusFilterId",
				pending.getId().toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].equipmentId").value(equipment.getId()))
				.andExpect(jsonPath("$[0].quantityRequested").value(2))
				.andExpect(jsonPath("$[0].requestStatusName").value("Pending"))
				.andExpect(jsonPath("$[0].createdByName").value("Integration Tester"));

		LocalDate expected = LocalDate.now().plusDays(14);
		EquipmentRequestDTO update = new EquipmentRequestDTO();
		update.setRequestStatusId(approved.getId());
		update.setExpectedReturnDate(expected);
		update.setModifiedBy(student.getId());
		mockMvc.perform(put("/EquipmentRequestServlet/{requestId}", request.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

		EquipmentRequest updated = requestRepository.findById(request.getId()).orElseThrow();
		assertEquals(approved.getId(), updated.getRequestStatus().getId());
		assertEquals(expected, updated.getExpectedReturnDate());
		assertNull(updated.getActualReturnDate());
		assertEquals(4, equipmentRepository.findById(equipment.getId()).orElseThrow().getQuantityLeft());
		// The requested quantity is less than the original stock, so the service leaves
		// availability unchanged.
		assertEquals(available.getId(),
				equipmentRepository.findById(equipment.getId()).orElseThrow().getAvailabilityType().getId());
		org.junit.jupiter.api.Assertions.assertNotEquals(lentOut.getId(), available.getId());

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.delete("/EquipmentRequestServlet/{requestId}", request.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
		org.junit.jupiter.api.Assertions.assertFalse(requestRepository.existsById(request.getId()));
	}
}
