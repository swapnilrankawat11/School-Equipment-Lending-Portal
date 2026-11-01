package com.schoolportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolportal.entity.EquipmentAvailabilityType;

public interface EquipmentAvailabilityTypeRepository extends JpaRepository<EquipmentAvailabilityType, Integer> {

	Optional<EquipmentAvailabilityType> findByAvailabilityStatusIgnoreCase(String statusName);

}
