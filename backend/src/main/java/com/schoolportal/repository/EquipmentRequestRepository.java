
package com.schoolportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schoolportal.entity.EquipmentRequest;

public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Integer> {

	@Query("""
			SELECT er FROM EquipmentRequest er
			JOIN FETCH er.equipment
			JOIN FETCH er.requestStatus
			WHERE er.requestStatus.id = :statusId
			ORDER BY er.createdAt DESC
			""")
	List<EquipmentRequest> findRequestsByStatusTypeId(@Param("statusId") int statusId);

	@Query("""
			SELECT er FROM EquipmentRequest er
			JOIN FETCH er.equipment
			JOIN FETCH er.requestStatus
			WHERE er.id = :requestId
			""")
	EquipmentRequest findRequestWithDetailsById(@Param("requestId") int requestId);

	@Query("""
			SELECT er FROM EquipmentRequest er
			JOIN FETCH er.equipment
			JOIN FETCH er.requestStatus
			WHERE er.createdBy = :studentId
			AND er.requestStatus.id = :statusId
			ORDER BY er.createdAt DESC
			""")
	List<EquipmentRequest> findRequestsByStudentAndStatus(@Param("studentId") int studentId,
			@Param("statusId") int statusId);

}
