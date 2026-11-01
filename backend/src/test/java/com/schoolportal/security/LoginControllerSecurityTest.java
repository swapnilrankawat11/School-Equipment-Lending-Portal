package com.schoolportal.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;
import com.schoolportal.repository.UserRepository;
import com.schoolportal.repository.UserTypeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User createUser(String email, String role) {
        UserType userType = userTypeRepository.findAll()
                .stream()
                .filter(type -> type.getUserType().equalsIgnoreCase(role))
                .findFirst()
                .orElseGet(() -> {
                    UserType newType = new UserType();
                    newType.setUserType(role);
                    return userTypeRepository.save(newType);
                });

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setUserType(userType);

        return userRepository.save(user);
    }

    @Test
    void login_withValidCredentials_shouldCreateSessionAndReturnUserData() throws Exception {
        createUser("login-test@example.com", "Student");

        mockMvc.perform(post("/LoginServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "login-test@example.com",
                               "password", "password123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.email").value("login-test@example.com"))
                .andExpect(jsonPath("$.user.fullName").value("Test User"))
                .andExpect(jsonPath("$.user.userTypeName").value("Student"))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andExpect(request().sessionAttribute("userId", org.hamcrest.Matchers.notNullValue()))
                .andExpect(request().sessionAttribute("userTypeName", org.hamcrest.Matchers.is("Student")));
    }

    @Test
    void login_withIncorrectPassword_shouldReturnUnauthorized() throws Exception {
        createUser("wrong-password@example.com", "Student");

        mockMvc.perform(post("/LoginServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "wrong-password@example.com",
                               "password", "wrongPassword"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void login_withUnknownEmail_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/LoginServlet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("email", "unknown@example.com",
                               "password", "password123"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }
}