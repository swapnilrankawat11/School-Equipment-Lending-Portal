
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

import com.schoolportal.dto.UserTypeDTO;
import com.schoolportal.entity.UserType;
import com.schoolportal.repository.UserTypeRepository;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeService userTypeService;

    private UserType userType;

    @BeforeEach
    void setUp() {

        userType = new UserType();

        userType.setId(1);
        userType.setUserType("Student");
        userType.setCreatedBy(10);
        userType.setModifiedBy(10);
    }

    @Test
    void getUserTypes_shouldReturnMappedDTOs() {

        when(userTypeRepository.findAll())
                .thenReturn(List.of(userType));

        List<UserTypeDTO> result = userTypeService.getUserTypes();

        assertEquals(1, result.size());

        UserTypeDTO dto = result.get(0);

        assertEquals(1, dto.getId());
        assertEquals("Student", dto.getUserType());
        assertEquals(10, dto.getCreatedBy());
        assertEquals(10, dto.getModifiedBy());
    }

    @Test
    void getUserTypes_emptyList_shouldReturnEmptyList() {

        when(userTypeRepository.findAll()).thenReturn(List.of());

        List<UserTypeDTO> result = userTypeService.getUserTypes();

        assertTrue(result.isEmpty());
    }
}
