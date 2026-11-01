package com.schoolportal.dto;

import java.time.LocalDateTime;

public class EquipmentAvailabilityTypeDTO {

	private Integer id;
	private Integer createdBy;
	private Integer modifiedBy;

	private String availabilityStatus;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public EquipmentAvailabilityTypeDTO(Integer id, String availabilityStatus) {
		this.id = id;
		this.availabilityStatus = availabilityStatus;
	}

	public EquipmentAvailabilityTypeDTO() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "EquipmentAvailabilityTypeDTO [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", availabilityStatus=" + availabilityStatus + ", description=" + description + ", createdAt="
				+ createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}