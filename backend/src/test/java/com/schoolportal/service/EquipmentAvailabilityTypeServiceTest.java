
package com.schoolportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolportal.dto.EquipmentAvailabilityTypeDTO;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;

@ExtendWith(MockitoExtension.class)
class EquipmentAvailabilityTypeServiceTest {

    @Mock
    private EquipmentAvailabilityTypeRepository repository;

    @InjectMocks
    private EquipmentAvailabilityTypeService availabilityTypeService;

    private EquipmentAvailabilityType availabilityType;

    @BeforeEach
    void setUp() {

        availabilityType = new EquipmentAvailabilityType();

        availabilityType.setId(1);
        availabilityType.setAvailabilityStatus("Available");
        availabilityType.setDescription("Equipment is available");
        availabilityType.setCreatedBy(10);
        availabilityType.setModifiedBy(10);
    }

    @Test
    void getAllEquipmentAvailabilityTypes_shouldReturnMappedDTOs() {

        when(repository.findAll())
                .thenReturn(List.of(availabilityType));

        List<EquipmentAvailabilityTypeDTO> result =
                availabilityTypeService.getAllEquipmentAvailabilityTypes();

        assertEquals(1, result.size());

        EquipmentAvailabilityTypeDTO dto = result.get(0);

        assertEquals(1, dto.getId());
        assertEquals("Available", dto.getAvailabilityStatus());
        assertEquals("Equipment is available", dto.getDescription());
        assertEquals(10, dto.getCreatedBy());
        assertEquals(10, dto.getModifiedBy());
    }

    @Test
    void getAllEquipmentAvailabilityTypes_emptyList_shouldReturnEmptyList() {

        when(repository.findAll()).thenReturn(List.of());

        List<EquipmentAvailabilityTypeDTO> result =
                availabilityTypeService.getAllEquipmentAvailabilityTypes();

        assertTrue(result.isEmpty());
    }
}
