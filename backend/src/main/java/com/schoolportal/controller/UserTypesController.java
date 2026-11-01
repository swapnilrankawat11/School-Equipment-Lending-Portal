
package com.schoolportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolportal.dto.UserTypeDTO;
import com.schoolportal.service.UserTypeService;

@RestController
@RequestMapping("/UserTypesServlet")
public class UserTypesController {

	private final UserTypeService userTypeService;

	public UserTypesController(UserTypeService userTypeService) {
		this.userTypeService = userTypeService;
	}

	@GetMapping
	public ResponseEntity<List<UserTypeDTO>> getUserTypes() {

		List<UserTypeDTO> userTypes = userTypeService.getUserTypes();

		return ResponseEntity.ok(userTypes);
	}
}
