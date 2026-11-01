package com.schoolportal.dto;

import java.time.LocalDateTime;

public class EquipmentDTO {

	private Integer id;
	private Integer categoryId;
	private Integer availabilityId;
	private Integer totalQuantity;
	private Integer quantityLeft;
	private Integer createdBy;
	private Integer modifiedBy;

	private String name;
	private String categoryName;
	private String availabilityStatus;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(Integer availabilityId) {
		this.availabilityId = availabilityId;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getQuantityLeft() {
		return quantityLeft;
	}

	public void setQuantityLeft(Integer quantityLeft) {
		this.quantityLeft = quantityLeft;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "EquipmentDTO [id=" + id + ", categoryId=" + categoryId + ", availabilityId=" + availabilityId
				+ ", totalQuantity=" + totalQuantity + ", quantityLeft=" + quantityLeft + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", name=" + name + ", categoryName=" + categoryName
				+ ", availabilityStatus=" + availabilityStatus + ", description=" + description + ", createdAt="
				+ createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}