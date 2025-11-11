package com.schoolportal.model;

public class SessionInfo {
	private int userId, userTypeId;
	private String userTypeName, firstName, lastName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
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
		return "SessionInfo [userId=" + userId + ", userTypeId=" + userTypeId + ", userTypeName=" + userTypeName
				+ ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
