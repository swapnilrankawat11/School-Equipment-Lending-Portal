
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

import com.schoolportal.dto.RequestStatusTypeDTO;
import com.schoolportal.entity.RequestStatusType;
import com.schoolportal.repository.RequestStatusTypeRepository;

@ExtendWith(MockitoExtension.class)
class RequestStatusTypeServiceTest {

    @Mock
    private RequestStatusTypeRepository statusRepository;

    @InjectMocks
    private RequestStatusTypeService statusService;

    private RequestStatusType status;

    @BeforeEach
    void setUp() {

        status = new RequestStatusType();

        status.setId(1);
        status.setStatusType("Pending");
        status.setDescription("Request awaiting approval");
        status.setCreatedBy(10);
        status.setModifiedBy(10);
    }

    @Test
    void getAllRequestStatusTypes_shouldReturnMappedDTOs() {

        when(statusRepository.findAll())
                .thenReturn(List.of(status));

        List<RequestStatusTypeDTO> result =
                statusService.getAllRequestStatusTypes();

        assertEquals(1, result.size());

        RequestStatusTypeDTO dto = result.get(0);

        assertEquals(1, dto.getId());
        assertEquals("Pending", dto.getStatusType());
        assertEquals("Request awaiting approval", dto.getDescription());
        assertEquals(10, dto.getCreatedBy());
        assertEquals(10, dto.getModifiedBy());
    }

    @Test
    void getAllRequestStatusTypes_emptyList_shouldReturnEmptyList() {

        when(statusRepository.findAll()).thenReturn(List.of());

        List<RequestStatusTypeDTO> result =
                statusService.getAllRequestStatusTypes();

        assertTrue(result.isEmpty());
    }
}
