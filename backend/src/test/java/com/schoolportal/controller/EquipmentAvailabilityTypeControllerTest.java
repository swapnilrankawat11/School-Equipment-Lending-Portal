package com.schoolportal.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.dto.EquipmentAvailabilityTypeDTO;
import com.schoolportal.service.EquipmentAvailabilityTypeService;

@WebMvcTest(EquipmentAvailabilityTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentAvailabilityTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private EquipmentAvailabilityTypeService equipmentAvailabilityTypeService;

	@Test
	void getAllEquipmentAvailabilityTypes_shouldReturnOkWithAvailabilityTypes() throws Exception {

		List<EquipmentAvailabilityTypeDTO> availabilityTypes = List.of(new EquipmentAvailabilityTypeDTO(1, "Available"),
				new EquipmentAvailabilityTypeDTO(2, "Unavailable"));

		when(equipmentAvailabilityTypeService.getAllEquipmentAvailabilityTypes()).thenReturn(availabilityTypes);

		mockMvc.perform(get("/EquipmentAvailabilityTypesServlet")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(availabilityTypes)));

		verify(equipmentAvailabilityTypeService).getAllEquipmentAvailabilityTypes();
	}

	@Test
	void getAllEquipmentAvailabilityTypes_shouldReturnEmptyListWhenNoTypesExist() throws Exception {

		when(equipmentAvailabilityTypeService.getAllEquipmentAvailabilityTypes()).thenReturn(List.of());

		mockMvc.perform(get("/EquipmentAvailabilityTypesServlet")).andExpect(status().isOk())
				.andExpect(content().json("[]"));

		verify(equipmentAvailabilityTypeService).getAllEquipmentAvailabilityTypes();
	}
}