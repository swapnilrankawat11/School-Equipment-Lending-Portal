package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.Equipment;
import com.schoolportal.util.DBConnection;

public class EquipmentDAO {

	public boolean addEquipment(Equipment eq) {
		String sql = "INSERT INTO equipments (name, category_id, availability_id, total_quantity, quantity_left, description, created_by, modified_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, eq.getName());
			ps.setInt(2, eq.getCategoryId());
			ps.setInt(3, eq.getAvailabilityId());
			ps.setInt(4, eq.getTotalQuantity());
			ps.setInt(5, eq.getTotalQuantity()); // same as total quantity initially
			ps.setString(6, eq.getDescription());
			ps.setInt(7, eq.getCreated_by());
			ps.setInt(8, eq.getCreated_by());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Equipment> getAllEquipments() {
		List<Equipment> list = new ArrayList<>();
		String sql = "SELECT e.id, e.name, e.category_id, e.availability_id, e.description, "
				+ "c.category_name, a.availability_status, "
				+ "e.total_quantity, e.quantity_left, e.created_by, e.created_at, e.modified_by, e.modified_at "
				+ "FROM equipments e " + "JOIN equipment_categories c ON e.category_id = c.id "
				+ "JOIN equipment_availability_types a ON e.availability_id = a.id " + "ORDER BY e.id ASC;";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Equipment eq = new Equipment();
				eq.setId(rs.getInt("id"));
				eq.setName(rs.getString("name"));
				eq.setCategoryId(rs.getInt("category_id"));
				eq.setAvailabilityId(rs.getInt("availability_id"));
				eq.setCategoryName(rs.getString("category_name"));
				eq.setAvailabilityStatus(rs.getString("availability_status"));
				eq.setTotalQuantity(rs.getInt("total_quantity"));
				eq.setQuantityLeft(rs.getInt("quantity_left"));
				eq.setDescription(rs.getString("description"));
				eq.setCreated_by(rs.getInt("created_by"));
				eq.setCreated_at(rs.getString("created_at"));
				eq.setModified_by(rs.getInt("modified_by"));
				eq.setModified_at(rs.getString("modified_at"));
				list.add(eq);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Equipment getEquipmentById(int equipmentId) {
		String sql = "SELECT * FROM equipments WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, equipmentId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Equipment eq = new Equipment();
					eq.setId(rs.getInt("id"));
					eq.setName(rs.getString("name"));
					eq.setCategoryId(rs.getInt("category_id"));
					eq.setAvailabilityId(rs.getInt("availability_id"));
					eq.setTotalQuantity(rs.getInt("total_quantity"));
					eq.setQuantityLeft(rs.getInt("quantity_left"));
					eq.setDescription(rs.getString("description"));
					eq.setCreated_by(rs.getInt("created_by"));
					eq.setCreated_at(rs.getString("created_at"));
					eq.setModified_by(rs.getInt("modified_by"));
					eq.setModified_at(rs.getString("modified_at"));
					return eq;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public boolean updateEquipment(int equipmentId, Equipment eq) {
		String sql = "UPDATE equipments SET name=?, category_id=?, availability_id=?, total_quantity=?, quantity_left = ?, description = ?, modified_by=?, modified_at=now() WHERE id=?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, eq.getName());
			ps.setInt(2, eq.getCategoryId());
			ps.setInt(3, eq.getAvailabilityId());
			ps.setInt(4, eq.getTotalQuantity());
			ps.setInt(5, eq.getQuantityLeft());
			ps.setString(6, eq.getDescription());
			ps.setInt(7, eq.getModified_by());
			ps.setInt(8, equipmentId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean deleteEquipment(int id) {
		String sql = "DELETE FROM equipments WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean updateEquipmentQuantityLeftById(int quantityLeft, int equipmentId) {
		String sql = "UPDATE equipments SET quantity_left = ? where id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, quantityLeft);
			ps.setInt(2, equipmentId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean updateAvailabilityStatusById(int statusId, int equipmentId) {
		String sql = "UPDATE equipments SET availability_id = ? where id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, statusId);
			ps.setInt(2, equipmentId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}