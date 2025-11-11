package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.EquipmentCategory;
import com.schoolportal.util.DBConnection;

public class EquipmentCategoryDAO {

	public boolean addEquipmentCategory(EquipmentCategory ec) {
		String sql = "INSERT INTO equipment_categories (category_name, description, created_by, modified_by) VALUES (?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, ec.getCategoryName());
			ps.setString(2, ec.getDescription());
			ps.setInt(3, ec.getCreatedBy());
			ps.setInt(4, ec.getCreatedBy());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<EquipmentCategory> getAllEquipmentCategories() {
		List<EquipmentCategory> list = new ArrayList<>();

		String sql = "SELECT * FROM equipment_categories";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				EquipmentCategory ec = new EquipmentCategory();
				ec.setId(rs.getInt("id"));
				ec.setCategoryName(rs.getString("category_name"));
				ec.setDescription(rs.getString("description"));
				ec.setCreatedBy(rs.getInt("created_by"));
				ec.setModifiedBy(rs.getInt("modified_by"));
				ec.setCreatedAt(rs.getString("created_at"));
				ec.setModifiedAt(rs.getString("modified_at"));
				list.add(ec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean updateEquipmentCategory(int categoryId, EquipmentCategory ec) {
		String sql = "UPDATE equipment_categories SET category_name=?, description=?, modified_by=?, modified_at=NOW() WHERE id=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, ec.getCategoryName());
			ps.setString(2, ec.getDescription());
			ps.setInt(3, ec.getModifiedBy());
			ps.setInt(4, categoryId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteEquipmentCategory(int categoryId) {
		String sql = "DELETE FROM equipment_categories WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
