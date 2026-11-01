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
import com.schoolportal.dto.EquipmentDTO;
import com.schoolportal.service.EquipmentService;

@WebMvcTest(EquipmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EquipmentService equipmentService;

    @Test
    void getAllEquipments_shouldReturnEquipments() throws Exception {

        List<EquipmentDTO> equipments = List.of(
                new EquipmentDTO(),
                new EquipmentDTO());

        when(equipmentService.getAllEquipments()).thenReturn(equipments);

        mockMvc.perform(get("/EquipmentServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(equipments)));

        verify(equipmentService).getAllEquipments();
    }

    @Test
    void getAllEquipments_shouldReturnEmptyList() throws Exception {

        when(equipmentService.getAllEquipments()).thenReturn(List.of());

        mockMvc.perform(get("/EquipmentServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(equipmentService).getAllEquipments();
    }

    @Test
    void addEquipment_shouldReturnSuccessTrue() throws Exception {

        EquipmentDTO equipment = new EquipmentDTO();

        when(equipmentService.addEquipment(any(EquipmentDTO.class))).thenReturn(true);

        mockMvc.perform(post("/EquipmentServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipment)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentService).addEquipment(any(EquipmentDTO.class));
    }

    @Test
    void addEquipment_shouldReturnSuccessFalse() throws Exception {

        EquipmentDTO equipment = new EquipmentDTO();

        when(equipmentService.addEquipment(any(EquipmentDTO.class))).thenReturn(false);

        mockMvc.perform(post("/EquipmentServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipment)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }

    @Test
    void updateEquipment_shouldReturnSuccessTrue() throws Exception {

        EquipmentDTO equipment = new EquipmentDTO();

        when(equipmentService.updateEquipment(
                eq(1), any(EquipmentDTO.class))).thenReturn(true);

        mockMvc.perform(put("/EquipmentServlet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipment)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentService).updateEquipment(
                eq(1), any(EquipmentDTO.class));
    }

    @Test
    void updateEquipment_shouldReturnSuccessFalse() throws Exception {

        EquipmentDTO equipment = new EquipmentDTO();

        when(equipmentService.updateEquipment(1, equipment)).thenReturn(false);

        mockMvc.perform(put("/EquipmentServlet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipment)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }

    @Test
    void deleteEquipment_shouldReturnSuccessTrue() throws Exception {

        when(equipmentService.deleteEquipment(1)).thenReturn(true);

        mockMvc.perform(delete("/EquipmentServlet/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentService).deleteEquipment(1);
    }

    @Test
    void deleteEquipment_shouldReturnSuccessFalse() throws Exception {

        when(equipmentService.deleteEquipment(1)).thenReturn(false);

        mockMvc.perform(delete("/EquipmentServlet/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }
}