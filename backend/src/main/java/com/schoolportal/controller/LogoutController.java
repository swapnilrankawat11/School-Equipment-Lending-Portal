
package com.schoolportal.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/LogoutServlet")
public class LogoutController {

	@PostMapping
	public ResponseEntity<Map<String, Boolean>> logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		return ResponseEntity.ok(Map.of("success", true));
	}
}
