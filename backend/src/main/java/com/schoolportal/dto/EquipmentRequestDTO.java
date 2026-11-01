package com.schoolportal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EquipmentRequestDTO {

	private Integer id;
	private Integer equipmentId;
	private Integer quantityRequested;
	private Integer requestStatusId;
	private Integer createdBy;
	private Integer modifiedBy;

	private String equipmentName;
	private String requestStatusName;
	private String purpose;
	private String createdByName;
	private String modifiedByName;

	private LocalDate expectedReturnDate;
	private LocalDate actualReturnDate;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Integer getQuantityRequested() {
		return quantityRequested;
	}

	public void setQuantityRequested(Integer quantityRequested) {
		this.quantityRequested = quantityRequested;
	}

	public Integer getRequestStatusId() {
		return requestStatusId;
	}

	public void setRequestStatusId(Integer requestStatusId) {
		this.requestStatusId = requestStatusId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getRequestStatusName() {
		return requestStatusName;
	}

	public void setRequestStatusName(String requestStatusName) {
		this.requestStatusName = requestStatusName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public LocalDate getExpectedReturnDate() {
		return expectedReturnDate;
	}

	public void setExpectedReturnDate(LocalDate expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}

	public LocalDate getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(LocalDate actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "EquipmentRequestDTO [id=" + id + ", equipmentId=" + equipmentId + ", quantityRequested="
				+ quantityRequested + ", requestStatusId=" + requestStatusId + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", equipmentName=" + equipmentName + ", requestStatusName="
				+ requestStatusName + ", purpose=" + purpose + ", createdByName=" + createdByName + ", modifiedByName="
				+ modifiedByName + ", expectedReturnDate=" + expectedReturnDate + ", actualReturnDate="
				+ actualReturnDate + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}