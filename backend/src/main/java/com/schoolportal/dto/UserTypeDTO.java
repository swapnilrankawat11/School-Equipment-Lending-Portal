package com.schoolportal.dto;

import java.time.LocalDateTime;

public class UserTypeDTO {

	private Integer id;
	private Integer createdBy;
	private Integer modifiedBy;

	private String userType;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public UserTypeDTO(int id, String userType) {
		this.id = id;
		this.userType = userType;
	}

	public UserTypeDTO() {
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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
		return "UserTypeDTO [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", userType="
				+ userType + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}
