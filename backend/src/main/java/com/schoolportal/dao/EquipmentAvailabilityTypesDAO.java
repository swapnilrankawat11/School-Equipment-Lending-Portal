package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.EquipmentAvailabilityTypes;
import com.schoolportal.util.DBConnection;

public class EquipmentAvailabilityTypesDAO {

	public List<EquipmentAvailabilityTypes> getAllEquipmentAvailabilityTypes() {
		List<EquipmentAvailabilityTypes> list = new ArrayList<>();

		String sql = "SELECT * FROM equipment_availability_types";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				EquipmentAvailabilityTypes at = new EquipmentAvailabilityTypes();
				at.setId(rs.getInt("id"));
				at.setAvailabilityStatus(rs.getString("availability_status"));
				at.setDescription(rs.getString("description"));
				at.setCreatedBy(rs.getInt("created_by"));
				at.setModifiedBy(rs.getInt("modified_by"));
				at.setCreatedAt(rs.getString("created_at"));
				at.setModifiedAt(rs.getString("modified_at"));
				list.add(at);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public int getAvailabilityStatusIdByName(String statusName) {
		String sql = "SELECT id from equipment_availability_types where availability_status ILIKE ?";
		int statusId = -1;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, statusName);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					statusId = rs.getInt("id");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusId;
	}

	public String getAvailabilityStatusNameById(int id) {
		String sql = "SELECT availability_status from equipment_availability_types where id = ?";
		String statusName = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					statusName = rs.getString("availability_status");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusName;
	}
}
