
package com.schoolportal.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.entity.User;
import com.schoolportal.service.AuthService;

@RestController
@RequestMapping("/LoginServlet")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> loginData,
            HttpServletRequest request) {

        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            User user = authService.login(email, password);

            // Create a new HTTP session
            HttpSession session = request.getSession(true);

            session.setAttribute("userId", user.getId());
            session.setAttribute("userTypeId", user.getUserType().getId());
            session.setAttribute(
                    "userTypeName", user.getUserType().getUserType());
            session.setAttribute("firstName", user.getFirstName());
            session.setAttribute("lastName", user.getLastName());

            // Session expires after 1 hour of inactivity
            session.setMaxInactiveInterval(60 * 60);

            // Prepare user response without exposing the password
            Map<String, Object> userData = new LinkedHashMap<>();

            userData.put("id", user.getId());
            userData.put("userTypeId", user.getUserType().getId());
            userData.put("userTypeName", user.getUserType().getUserType());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("fullName",
                    user.getFirstName() + " " + user.getLastName());
            userData.put("email", user.getEmail());

            userData.put("createdBy", user.getCreatedBy());
            userData.put("modifiedBy", user.getModifiedBy());
            userData.put("createdAt", user.getCreatedAt());
            userData.put("modifiedAt", user.getModifiedAt());

            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("success", true);
            responseBody.put("user", userData);

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {

            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }
}
