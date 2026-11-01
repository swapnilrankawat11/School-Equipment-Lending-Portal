package com.schoolportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schoolportal.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

	@Query("""
			SELECT e FROM Equipment e
			JOIN FETCH e.category
			JOIN FETCH e.availabilityType
			ORDER BY e.id ASC
			""")
	List<Equipment> findAllWithDetails();

	@Modifying(clearAutomatically = true)
	@Query("""
			UPDATE Equipment e
			SET e.quantityLeft = :quantityLeft
			WHERE e.id = :equipmentId
			""")
	int updateEquipmentQuantityLeftById(@Param("quantityLeft") int quantityLeft, @Param("equipmentId") int equipmentId);

	@Modifying(clearAutomatically = true)
	@Query("""
			UPDATE Equipment e
			SET e.availabilityType.id = :statusId
			WHERE e.id = :equipmentId
			""")
	int updateAvailabilityStatusById(@Param("statusId") int statusId, @Param("equipmentId") int equipmentId);

}
