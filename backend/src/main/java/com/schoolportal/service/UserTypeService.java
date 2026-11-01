
package com.schoolportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.UserTypeDTO;
import com.schoolportal.entity.UserType;
import com.schoolportal.repository.UserTypeRepository;

@Service
public class UserTypeService {

	private final UserTypeRepository userTypeRepository;

	public UserTypeService(UserTypeRepository userTypeRepository) {
		this.userTypeRepository = userTypeRepository;
	}

	@Transactional(readOnly = true)
	public List<UserTypeDTO> getUserTypes() {

		List<UserType> userTypes = userTypeRepository.findAll();

		return userTypes.stream().map(userType -> {

			UserTypeDTO dto = new UserTypeDTO();

			dto.setId(userType.getId());
			dto.setUserType(userType.getUserType());

			dto.setCreatedBy(userType.getCreatedBy());
			dto.setModifiedBy(userType.getModifiedBy());

			dto.setCreatedAt(userType.getCreatedAt());
			dto.setModifiedAt(userType.getModifiedAt());

			return dto;

		}).toList();
	}
}
