package com.schoolportal.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.schoolportal.dto.EquipmentCategoryDTO;
import com.schoolportal.service.EquipmentCategoryService;

@WebMvcTest(EquipmentCategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EquipmentCategoryService equipmentCategoryService;

    @Test
    void getAllEquipmentCategories_shouldReturnCategories() throws Exception {

        List<EquipmentCategoryDTO> categories = List.of(
                new EquipmentCategoryDTO(1, "Electronics"),
                new EquipmentCategoryDTO(2, "Furniture"));

        when(equipmentCategoryService.getAllEquipmentCategories()).thenReturn(categories);

        mockMvc.perform(get("/EquipmentCategoryServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categories)));

        verify(equipmentCategoryService).getAllEquipmentCategories();
    }

    @Test
    void getAllEquipmentCategories_shouldReturnEmptyList() throws Exception {

        when(equipmentCategoryService.getAllEquipmentCategories()).thenReturn(List.of());

        mockMvc.perform(get("/EquipmentCategoryServlet"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(equipmentCategoryService).getAllEquipmentCategories();
    }

    @Test
    void addEquipmentCategory_shouldReturnSuccessTrue() throws Exception {

        EquipmentCategoryDTO category = new EquipmentCategoryDTO(1, "Electronics");

        when(equipmentCategoryService.addEquipmentCategory(any(EquipmentCategoryDTO.class)))
                .thenReturn(true);

        mockMvc.perform(post("/EquipmentCategoryServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentCategoryService).addEquipmentCategory(any(EquipmentCategoryDTO.class));
    }

    @Test
    void addEquipmentCategory_shouldReturnSuccessFalse() throws Exception {

        EquipmentCategoryDTO category = new EquipmentCategoryDTO(1, "Electronics");

        when(equipmentCategoryService.addEquipmentCategory(any(EquipmentCategoryDTO.class)))
                .thenReturn(false);

        mockMvc.perform(post("/EquipmentCategoryServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }

    @Test
    void updateEquipmentCategory_shouldReturnSuccessTrue() throws Exception {

        EquipmentCategoryDTO category = new EquipmentCategoryDTO(1, "Electronics");

        when(equipmentCategoryService.updateEquipmentCategory(
                org.mockito.ArgumentMatchers.eq(1),
                any(EquipmentCategoryDTO.class))).thenReturn(true);

        mockMvc.perform(put("/EquipmentCategoryServlet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentCategoryService).updateEquipmentCategory(
                org.mockito.ArgumentMatchers.eq(1),
                any(EquipmentCategoryDTO.class));
    }

    @Test
    void updateEquipmentCategory_shouldReturnSuccessFalse() throws Exception {

        EquipmentCategoryDTO category = new EquipmentCategoryDTO(1, "Electronics");

        when(equipmentCategoryService.updateEquipmentCategory(1, category)).thenReturn(false);

        mockMvc.perform(put("/EquipmentCategoryServlet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }

    @Test
    void deleteEquipmentCategory_shouldReturnSuccessTrue() throws Exception {

        when(equipmentCategoryService.deleteEquipmentCategory(1)).thenReturn(true);

        mockMvc.perform(delete("/EquipmentCategoryServlet/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}"));

        verify(equipmentCategoryService).deleteEquipmentCategory(1);
    }

    @Test
    void deleteEquipmentCategory_shouldReturnSuccessFalse() throws Exception {

        when(equipmentCategoryService.deleteEquipmentCategory(1)).thenReturn(false);

        mockMvc.perform(delete("/EquipmentCategoryServlet/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":false}"));
    }
}