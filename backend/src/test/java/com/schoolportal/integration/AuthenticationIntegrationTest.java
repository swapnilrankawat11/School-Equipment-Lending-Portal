package com.schoolportal.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;

class AuthenticationIntegrationTest extends AbstractIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void signupLoginSessionInfoAndLogoutUsePersistedCredentials() throws Exception {
        UserType studentType = newStandardUserType("Student");
        String email = unique("auth-student") + "@example.test";
        String password = "secure-pass-123";

        mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("Asha", "Student", email, password, studentType)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

        User saved = userRepository.findByEmail(email).orElseThrow();
        trackUser(saved);
        assertNotEquals(password, saved.getPassword());
        assertTrue(passwordEncoder.matches(password, saved.getPassword()));

        mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("Asha", "Student", email, password, studentType)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already in use."));

        MvcResult login = mockMvc.perform(post("/LoginServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(email, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.email").value(email))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andReturn();
        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        assertTrue(session != null);
        assertTrue(session.getMaxInactiveInterval() == 60 * 60);
        assertTrue(session.getAttribute("userId").equals(saved.getId()));

        mockMvc.perform(get("/SessionInfoServlet").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(saved.getId()))
                .andExpect(jsonPath("$.userTypeName").value("Student"));

        mockMvc.perform(post("/LogoutServlet").session(session))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        org.junit.jupiter.api.Assertions.assertTrue(session.isInvalid());
        mockMvc.perform(get("/SessionInfoServlet"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("No active session"));
    }

    @Test
    void invalidPasswordAndUnknownEmailAreRejected() throws Exception {
        UserType studentType = newStandardUserType("Student");
        String email = unique("auth-invalid") + "@example.test";
        mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("Asha", "Student", email, "secure-pass-123", studentType)))
                .andExpect(status().isOk());
        trackUser(userRepository.findByEmail(email).orElseThrow());

        mockMvc.perform(post("/LoginServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(email, "wrong-password")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password."));
        mockMvc.perform(post("/LoginServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(unique("unknown") + "@example.test", "secure-pass-123")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password."));
    }

    @Test
    void signupAcceptsAdminUserTypeSubmittedByCaller() throws Exception {
        UserType adminType = newStandardUserType("Admin");
        String email = unique("self-assigned-admin") + "@example.test";
        mockMvc.perform(post("/SignupServlet").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson("New", "Admin", email, "secure-pass-123", adminType)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

        User saved = userRepository.findByEmail(email).orElseThrow();
        trackUser(saved);
        assertEquals(
        	    "Admin",
        	    userTypeRepository.findById(saved.getUserType().getId())
        	        .orElseThrow()
        	        .getUserType()
        	);
    }

    private String userJson(String first, String last, String email, String password, UserType type)
            throws Exception {
        User user = new User();
        user.setFirstName(first);
        user.setLastName(last);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(type);
        return objectMapper.writeValueAsString(user);
    }

    private String credentials(String email, String password) throws Exception {
        return objectMapper.writeValueAsString(java.util.Map.of("email", email, "password", password));
    }
}
