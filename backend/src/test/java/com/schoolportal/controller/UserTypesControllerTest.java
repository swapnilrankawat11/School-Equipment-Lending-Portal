package com.schoolportal.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.dto.UserTypeDTO;
import com.schoolportal.service.UserTypeService;

@WebMvcTest(UserTypesController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserTypesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private UserTypeService userTypeService;

	@Test
	void getUserTypes_shouldReturnUserTypes() throws Exception {

		List<UserTypeDTO> userTypes = List.of(new UserTypeDTO(1, "Admin"), new UserTypeDTO(2, "Staff"),
				new UserTypeDTO(3, "Student"));

		when(userTypeService.getUserTypes()).thenReturn(userTypes);

		mockMvc.perform(get("/UserTypesServlet")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(userTypes)));

		verify(userTypeService).getUserTypes();
	}

	@Test
	void getUserTypes_shouldReturnEmptyList() throws Exception {

		when(userTypeService.getUserTypes()).thenReturn(List.of());

		mockMvc.perform(get("/UserTypesServlet")).andExpect(status().isOk()).andExpect(content().json("[]"));

		verify(userTypeService).getUserTypes();
	}
}