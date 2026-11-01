package com.schoolportal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.dto.EquipmentRequestDTO;
import com.schoolportal.service.EquipmentRequestService;

@WebMvcTest(EquipmentRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentRequestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private EquipmentRequestService equipmentRequestService;

	@Test
	void createEquipmentRequest_shouldReturnSuccessTrue() throws Exception {

		EquipmentRequestDTO request = new EquipmentRequestDTO();

		when(equipmentRequestService.createEquipmentRequest(any(EquipmentRequestDTO.class))).thenReturn(true);

		mockMvc.perform(post("/EquipmentRequestServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":true}"));

		verify(equipmentRequestService).createEquipmentRequest(any(EquipmentRequestDTO.class));
	}

	@Test
	void createEquipmentRequest_shouldReturnSuccessFalse() throws Exception {

		EquipmentRequestDTO request = new EquipmentRequestDTO();

		when(equipmentRequestService.createEquipmentRequest(any(EquipmentRequestDTO.class))).thenReturn(false);

		mockMvc.perform(post("/EquipmentRequestServlet").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":false}"));
	}

	@Test
	void getEquipmentRequests_withoutStatusFilter_shouldReturnRequestsByStatus() throws Exception {

		List<EquipmentRequestDTO> requests = List.of(new EquipmentRequestDTO());

		when(equipmentRequestService.getEquipmentRequestsByStatusType(1)).thenReturn(requests);

		mockMvc.perform(get("/EquipmentRequestServlet/1")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(requests)));

		verify(equipmentRequestService).getEquipmentRequestsByStatusType(1);
	}

	@Test
	void getEquipmentRequests_withStatusFilter_shouldReturnStudentRequestsByStatus() throws Exception {

		List<EquipmentRequestDTO> requests = List.of(new EquipmentRequestDTO());

		when(equipmentRequestService.getEquipmentRequestsByStudentAndStatus(1, 2)).thenReturn(requests);

		mockMvc.perform(get("/EquipmentRequestServlet/1").param("statusFilterId", "2")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(requests)));

		verify(equipmentRequestService).getEquipmentRequestsByStudentAndStatus(1, 2);
	}

	@Test
	void getEquipmentRequests_shouldReturnEmptyListWhenNoRequestsExist() throws Exception {

		when(equipmentRequestService.getEquipmentRequestsByStatusType(1)).thenReturn(List.of());

		mockMvc.perform(get("/EquipmentRequestServlet/1")).andExpect(status().isOk()).andExpect(content().json("[]"));

		verify(equipmentRequestService).getEquipmentRequestsByStatusType(1);
	}

	@Test
	void updateEquipmentRequest_shouldReturnSuccessTrue() throws Exception {

		EquipmentRequestDTO request = new EquipmentRequestDTO();

		when(equipmentRequestService.updateEquipmentRequest(eq(1), any(EquipmentRequestDTO.class))).thenReturn(true);

		mockMvc.perform(put("/EquipmentRequestServlet/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":true}"));

		verify(equipmentRequestService).updateEquipmentRequest(eq(1), any(EquipmentRequestDTO.class));
	}

	@Test
	void updateEquipmentRequest_shouldReturnSuccessFalse() throws Exception {

		EquipmentRequestDTO request = new EquipmentRequestDTO();

		when(equipmentRequestService.updateEquipmentRequest(eq(1), any(EquipmentRequestDTO.class))).thenReturn(false);

		mockMvc.perform(put("/EquipmentRequestServlet/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":false}"));
	}

	@Test
	void deleteEquipmentRequest_shouldReturnSuccessTrue() throws Exception {

		when(equipmentRequestService.deleteEquipmentRequest(1)).thenReturn(true);

		mockMvc.perform(delete("/EquipmentRequestServlet/1")).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":true}"));

		verify(equipmentRequestService).deleteEquipmentRequest(1);
	}

	@Test
	void deleteEquipmentRequest_shouldReturnSuccessFalse() throws Exception {

		when(equipmentRequestService.deleteEquipmentRequest(1)).thenReturn(false);

		mockMvc.perform(delete("/EquipmentRequestServlet/1")).andExpect(status().isOk())
				.andExpect(content().json("{\"success\":false}"));
	}
}