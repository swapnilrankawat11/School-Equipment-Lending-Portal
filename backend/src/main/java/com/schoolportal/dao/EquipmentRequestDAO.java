package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.EquipmentRequest;
import com.schoolportal.util.DBConnection;

public class EquipmentRequestDAO {

	public boolean createEquipmentRequest(EquipmentRequest eq) {
		String sql = "INSERT INTO equipment_requests (equipment_id, quantity_requested, purpose, created_by, modified_by) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, eq.getEquipmentId());
			ps.setInt(2, eq.getQuantityRequested());
			ps.setString(3, eq.getPurpose());
			ps.setInt(4, eq.getCreatedBy());
			ps.setInt(5, eq.getModifiedBy());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<EquipmentRequest> getEquipmentRequestsByStatusType(int requestStatusTypeId) {
		List<EquipmentRequest> list = new ArrayList<>();

		String sql = "SELECT er.*, " + "e.name AS equipment_name, " + "rst.status_type AS request_status_name, "
				+ "CONCAT(cu.first_name, ' ', cu.last_name) AS created_by_name, "
				+ "CONCAT(mu.first_name, ' ', mu.last_name) AS modified_by_name " + "FROM equipment_requests er "
				+ "JOIN equipments e ON er.equipment_id = e.id "
				+ "JOIN request_status_types rst ON er.request_status_id = rst.id "
				+ "LEFT JOIN users cu ON er.created_by = cu.id " + "LEFT JOIN users mu ON er.modified_by = mu.id "
				+ "WHERE er.request_status_id = ? " + "ORDER BY er.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, requestStatusTypeId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					EquipmentRequest req = new EquipmentRequest();
					req.setId(rs.getInt("id"));
					req.setEquipmentId(rs.getInt("equipment_id"));
					req.setQuantityRequested(rs.getInt("quantity_requested"));
					req.setPurpose(rs.getString("purpose"));
					req.setRequestStatusId(rs.getInt("request_status_id"));
					req.setRequestStatusName(rs.getString("request_status_name"));
					req.setExpectedReturnDate(rs.getString("expected_return_date"));
					req.setCreatedByName(rs.getString("created_by_name"));
					req.setModifiedByName(rs.getString("modified_by_name"));
					req.setActualReturnDate(rs.getString("actual_return_date"));
					req.setCreatedBy(rs.getInt("created_by"));
					req.setCreatedAt(rs.getString("created_at"));
					req.setModifiedBy(rs.getInt("modified_by"));
					req.setModifiedAt(rs.getString("modified_at"));
					req.setEquipmentName(rs.getString("equipment_name"));
					list.add(req);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public EquipmentRequest getEquipmentRequestById(int requestId) {
		String sql = "SELECT * FROM equipment_requests WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, requestId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					EquipmentRequest eqReq = new EquipmentRequest();
					eqReq.setId(rs.getInt("id"));
					eqReq.setEquipmentId(rs.getInt("equipment_id"));
					eqReq.setQuantityRequested(rs.getInt("quantity_requested"));
					eqReq.setPurpose(rs.getString("purpose"));
					eqReq.setRequestStatusId(rs.getInt("request_status_id"));
					eqReq.setExpectedReturnDate(rs.getString("expected_return_date"));
					eqReq.setCreatedBy(rs.getInt("created_by"));
					eqReq.setCreatedAt(rs.getString("created_at"));
					eqReq.setModifiedAt(rs.getString("modified_at"));
					eqReq.setModifiedBy(rs.getInt("modified_by"));
					eqReq.setActualReturnDate(rs.getString("actual_return_date"));
					return eqReq;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean updateEquipmentRequest(int requestId, EquipmentRequest rq) {
		String sql = "UPDATE equipment_requests SET request_status_id = ?, expected_return_date = ?, actual_return_date = ?, modified_by = ?, modified_at = now() WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, rq.getRequestStatusId());

			if (rq.getExpectedReturnDate() != null && !rq.getExpectedReturnDate().isEmpty()) {
				ps.setDate(2, java.sql.Date.valueOf(rq.getExpectedReturnDate())); // must be "yyyy-MM-dd" format
			} else {
				ps.setNull(2, java.sql.Types.DATE);
			}

			if (rq.getActualReturnDate() != null && !rq.getActualReturnDate().isEmpty()) {
				ps.setDate(3, java.sql.Date.valueOf(rq.getActualReturnDate()));
			} else {
				ps.setNull(3, java.sql.Types.DATE);
			}

			ps.setInt(4, rq.getModifiedBy());
			ps.setInt(5, requestId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<EquipmentRequest> getEquipmentRequestsByStudentAndStatus(int studentId, int statusTypeId) {
		List<EquipmentRequest> list = new ArrayList<>();
		String sql = "SELECT er.*, " + "e.name AS equipment_name, " + "rst.status_type AS request_status_name, "
				+ "CONCAT(cu.first_name, ' ', cu.last_name) AS created_by_name, "
				+ "CONCAT(mu.first_name, ' ', mu.last_name) AS modified_by_name " + "FROM equipment_requests er "
				+ "JOIN equipments e ON er.equipment_id = e.id "
				+ "JOIN request_status_types rst ON er.request_status_id = rst.id "
				+ "LEFT JOIN users cu ON er.created_by = cu.id " + "LEFT JOIN users mu ON er.modified_by = mu.id "
				+ "WHERE er.created_by = ? AND er.request_status_id = ? " + "ORDER BY er.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, studentId);
			ps.setInt(2, statusTypeId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					EquipmentRequest req = new EquipmentRequest();
					req.setId(rs.getInt("id"));
					req.setEquipmentId(rs.getInt("equipment_id"));
					req.setQuantityRequested(rs.getInt("quantity_requested"));
					req.setPurpose(rs.getString("purpose"));
					req.setRequestStatusId(rs.getInt("request_status_id"));
					req.setRequestStatusName(rs.getString("request_status_name"));
					req.setExpectedReturnDate(rs.getString("expected_return_date"));
					req.setCreatedByName(rs.getString("created_by_name"));
					req.setModifiedByName(rs.getString("modified_by_name"));
					req.setActualReturnDate(rs.getString("actual_return_date"));
					req.setCreatedBy(rs.getInt("created_by"));
					req.setCreatedAt(rs.getString("created_at"));
					req.setModifiedBy(rs.getInt("modified_by"));
					req.setModifiedAt(rs.getString("modified_at"));
					req.setEquipmentName(rs.getString("equipment_name"));
					list.add(req);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean deleteEquipmentRequest(int requestId) {
		String sql = "DELETE FROM equipment_requests WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, requestId);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
