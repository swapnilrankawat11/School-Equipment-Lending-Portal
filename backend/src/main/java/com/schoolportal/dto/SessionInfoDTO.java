package com.schoolportal.dto;

public class SessionInfoDTO {

	private Integer userId;
	private Integer userTypeId;

	private String userTypeName;
	private String firstName;
	private String lastName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "SessionInfoDTO [userId=" + userId + ", userTypeId=" + userTypeId + ", userTypeName=" + userTypeName
				+ ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}