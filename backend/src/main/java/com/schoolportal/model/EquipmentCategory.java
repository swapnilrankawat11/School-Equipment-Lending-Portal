package com.schoolportal.model;

public class EquipmentCategory {
	private int id, createdBy, modifiedBy;
	private String categoryName, description, createdAt, modifiedAt;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
	public int getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(int modifiedBy) {
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
	
	public String getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getModifiedAt() {
		return modifiedAt;
	}
	
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "EquipmentCategory [id=" + id + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", categoryName=" + categoryName + ", description=" + description + ", createdAt=" + createdAt
				+ ", modifiedAt=" + modifiedAt + "]";
	}
	
}
