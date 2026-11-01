package com.schoolportal.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;
import com.schoolportal.service.AuthService;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_shouldReturnUserDetailsAndCreateSession() throws Exception {

        UserType userType = new UserType();
        userType.setId(1);
        userType.setUserType("Admin");

        User user = new User();
        user.setId(1);
        user.setUserType(userType);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");

        when(authService.login("john@example.com", "password123"))
                .thenReturn(user);

        mockMvc.perform(post("/LoginServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "john@example.com", "password", "password123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.userTypeName").value("Admin"))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.user.fullName").value("John Doe"))
                .andExpect(jsonPath("$.user.email").value("john@example.com"))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andExpect(jsonPath("$.user.createdBy").doesNotExist());

        verify(authService).login("john@example.com", "password123");
    }

    @Test
    void login_shouldReturnUnauthorizedWhenAuthenticationFails() throws Exception {

        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/LoginServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "wrong@example.com", "password", "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }
}