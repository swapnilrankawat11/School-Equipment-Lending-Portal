package com.schoolportal.dao;

import java.sql.*;
import com.schoolportal.model.User;
import com.schoolportal.util.DBConnection;

public class UserDAO {

	public boolean createUser(User user) {
		String sql = "INSERT INTO users (first_name, last_name, user_type_id, email, password) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setInt(3, user.getUserTypeId());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPassword());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public User getUserByEmail(String email) {
		String sql = "SELECT u.*, ut.user_type AS user_type_name " + "FROM users u "
				+ "JOIN user_types ut ON u.user_type_id = ut.id " + "WHERE u.email = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					user.setUserTypeId(rs.getInt("user_type_id"));
					user.setUserTypeName(rs.getString("user_type_name"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setCreatedBy(rs.getInt("created_by"));
					user.setModifiedBy(rs.getInt("modified_by"));
					user.setCreatedAt(rs.getString("created_at"));
					user.setModifiedAt(rs.getString("modified_at"));
					return user;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserById(int id) {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					user.setUserTypeId(rs.getInt("user_type_id"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setCreatedBy(rs.getInt("created_by"));
					user.setModifiedBy(rs.getInt("modified_by"));
					user.setCreatedAt(rs.getString("created_at"));
					user.setModifiedAt(rs.getString("modified_at"));
					return user;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
