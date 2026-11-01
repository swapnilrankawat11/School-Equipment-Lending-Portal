
package com.schoolportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.dto.EquipmentAvailabilityTypeDTO;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;

@Service
@Transactional(readOnly = true)
public class EquipmentAvailabilityTypeService {

	private final EquipmentAvailabilityTypeRepository repository;

	public EquipmentAvailabilityTypeService(EquipmentAvailabilityTypeRepository repository) {
		this.repository = repository;
	}

	public List<EquipmentAvailabilityTypeDTO> getAllEquipmentAvailabilityTypes() {

		List<EquipmentAvailabilityType> availabilityTypes = repository.findAll();

		return availabilityTypes.stream().map(type -> {

			EquipmentAvailabilityTypeDTO dto = new EquipmentAvailabilityTypeDTO();

			dto.setId(type.getId());
			dto.setCreatedBy(type.getCreatedBy());
			dto.setModifiedBy(type.getModifiedBy());
			dto.setAvailabilityStatus(type.getAvailabilityStatus());
			dto.setDescription(type.getDescription());
			dto.setCreatedAt(type.getCreatedAt());
			dto.setModifiedAt(type.getModifiedAt());

			return dto;

		}).toList();
	}
}
