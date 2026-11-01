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
import com.schoolportal.dto.RequestStatusTypeDTO;
import com.schoolportal.service.RequestStatusTypeService;

@WebMvcTest(RequestStatusTypesController.class)
@AutoConfigureMockMvc(addFilters = false)
class RequestStatusTypesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RequestStatusTypeService statusService;

    @Test
    void getAllRequestStatusTypes_shouldReturnStatuses() throws Exception {

        List<RequestStatusTypeDTO> statuses = List.of(
                new RequestStatusTypeDTO(1, "Pending"),
                new RequestStatusTypeDTO(2, "Approved"));

        when(statusService.getAllRequestStatusTypes()).thenReturn(statuses);

        mockMvc.perform(get("/RequestStatusTypesServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statuses)));

        verify(statusService).getAllRequestStatusTypes();
    }

    @Test
    void getAllRequestStatusTypes_shouldReturnEmptyList() throws Exception {

        when(statusService.getAllRequestStatusTypes()).thenReturn(List.of());

        mockMvc.perform(get("/RequestStatusTypesServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(statusService).getAllRequestStatusTypes();
    }
}