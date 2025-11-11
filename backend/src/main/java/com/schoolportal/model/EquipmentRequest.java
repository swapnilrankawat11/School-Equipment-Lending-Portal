package com.schoolportal.model;

public class EquipmentRequest {
	private int id, equipmentId, quantityRequested, requestStatusId, createdBy, modifiedBy;
	private String equipmentName, requestStatusName, purpose, expectedReturnDate, actualReturnDate, createdAt,
			modifiedAt, createdByName, modifiedByName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getQuantityRequested() {
		return quantityRequested;
	}

	public void setQuantityRequested(int quantityRequested) {
		this.quantityRequested = quantityRequested;
	}

	public int getRequestStatusId() {
		return requestStatusId;
	}

	public void setRequestStatusId(int requestStatusId) {
		this.requestStatusId = requestStatusId;
	}

	public String getRequestStatusName() {
		return requestStatusName;
	}

	public void setRequestStatusName(String requestStatusName) {
		this.requestStatusName = requestStatusName;
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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getExpectedReturnDate() {
		return expectedReturnDate;
	}

	public void setExpectedReturnDate(String expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}

	public String getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(String actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
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

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
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

	@Override
	public String toString() {
		return "EquipmentRequest [id=" + id + ", equipmentId=" + equipmentId + ", quantityRequested="
				+ quantityRequested + ", requestStatusId=" + requestStatusId + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", equipmentName=" + equipmentName + ", requestStatusName="
				+ requestStatusName + ", purpose=" + purpose + ", expectedReturnDate=" + expectedReturnDate
				+ ", actualReturnDate=" + actualReturnDate + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt
				+ ", createdByName=" + createdByName + ", modifiedByName=" + modifiedByName + "]";
	}

}
