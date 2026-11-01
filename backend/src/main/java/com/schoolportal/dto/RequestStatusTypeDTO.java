package com.schoolportal.dto;

import java.time.LocalDateTime;

public class RequestStatusTypeDTO {

	private Integer id;
	private Integer createdBy;
	private Integer modifiedBy;

	private String statusType;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public RequestStatusTypeDTO(int id, String statusType) {
		this.id = id;
		this.statusType = statusType;

	}

	public RequestStatusTypeDTO() {
		// TODO Auto-generated constructor stub
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

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
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
		return "RequestStatusTypeDTO [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", statusType=" + statusType + ", description=" + description + ", createdAt=" + createdAt
				+ ", modifiedAt=" + modifiedAt + "]";
	}

}