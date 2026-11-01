
package com.schoolportal.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.SessionInfoDTO;

@RestController
@RequestMapping("/SessionInfoServlet")
public class SessionInfoController {

    @GetMapping
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("userId") != null) {

            SessionInfoDTO sessionInfo = new SessionInfoDTO();

            sessionInfo.setUserId((Integer) session.getAttribute("userId"));
            sessionInfo.setUserTypeId((Integer) session.getAttribute("userTypeId"));
            sessionInfo.setUserTypeName((String) session.getAttribute("userTypeName"));
            sessionInfo.setFirstName((String) session.getAttribute("firstName"));
            sessionInfo.setLastName((String) session.getAttribute("lastName"));

            return ResponseEntity.ok(sessionInfo);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "No active session"));
    }
}
