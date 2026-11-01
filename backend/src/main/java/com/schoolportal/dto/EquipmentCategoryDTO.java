package com.schoolportal.dto;

import java.time.LocalDateTime;

public class EquipmentCategoryDTO {

	private Integer id;
	private Integer createdBy;
	private Integer modifiedBy;

	private String categoryName;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public EquipmentCategoryDTO(int id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
	}

	public EquipmentCategoryDTO() {
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
		return "EquipmentCategoryDTO [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", categoryName=" + categoryName + ", description=" + description + ", createdAt=" + createdAt
				+ ", modifiedAt=" + modifiedAt + "]";
	}

}