
package com.schoolportal.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.entity.User;
import com.schoolportal.service.AuthService;

@RestController
@RequestMapping("/SignupServlet")
public class SignupController {

	private final AuthService authService;

	public SignupController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) {

		try {

			boolean result = authService.signup(user);

			return ResponseEntity.ok(Map.of("success", result));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}
}
