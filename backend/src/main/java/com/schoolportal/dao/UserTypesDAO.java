package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.UserType;
import com.schoolportal.util.DBConnection;

public class UserTypesDAO {

	public List<UserType> getUserTypes() {
		String sql = "SELECT * FROM user_types";
		List<UserType> list = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				UserType userType = new UserType();
				userType.setId(rs.getInt("id"));
				userType.setUserType(rs.getString("user_type"));
				userType.setCreatedBy(rs.getInt("created_by"));
				userType.setCreatedAt(rs.getString("created_at"));
				userType.setModifiedBy(rs.getInt("modified_by"));
				userType.setModifiedAt(rs.getString("modified_at"));
				list.add(userType);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
