package com.schoolportal.model;

public class Equipment {
	private int id, categoryId, availabilityId, totalQuantity, quantityLeft, created_by, modified_by;
	private String name, categoryName, availabilityStatus, created_at, modified_at, description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}


	public int getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getQuantityLeft() {
		return quantityLeft;
	}

	public void setQuantityLeft(int quantityLeft) {
		this.quantityLeft = quantityLeft;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getModified_by() {
		return modified_by;
	}

	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getModified_at() {
		return modified_at;
	}

	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Equipment [id=" + id + ", categoryId=" + categoryId + ", availabilityId=" + availabilityId
				+ ", totalQuantity=" + totalQuantity + ", quantityLeft=" + quantityLeft + ", created_by=" + created_by
				+ ", modified_by=" + modified_by + ", name=" + name + ", categoryName=" + categoryName
				+ ", availabilityStatus=" + availabilityStatus + ", created_at=" + created_at + ", modified_at="
				+ modified_at + ", description=" + description + "]";
	}

	

}