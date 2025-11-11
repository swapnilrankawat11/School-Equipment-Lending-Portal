package com.schoolportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.schoolportal.model.RequestStatusTypes;
import com.schoolportal.util.DBConnection;

public class RequestStatusTypesDAO {
	public List<RequestStatusTypes> getAllRequestStatusTypes() {
		List<RequestStatusTypes> list = new ArrayList<>();

		String sql = "SELECT * FROM request_status_types";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				RequestStatusTypes rq = new RequestStatusTypes();
				rq.setId(rs.getInt("id"));
				rq.setStatus_type(rs.getString("status_type"));
				rq.setDescription(rs.getString("description"));
				rq.setCreatedBy(rs.getInt("created_by"));
				rq.setCreatedAt(rs.getString("created_by"));
				rq.setModifiedBy(rs.getInt("modified_by"));
				rq.setModifiedAt(rs.getString("modified_at"));
				list.add(rq);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public RequestStatusTypes getRequestStatusTypeById(int id) {
		String sql = "SELECT * FROM request_status_types WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					RequestStatusTypes st = new RequestStatusTypes();
					st.setId(rs.getInt("id"));
					st.setStatus_type(rs.getString("status_type"));
					st.setDescription(rs.getString("description"));
					st.setCreatedBy(rs.getInt("created_by"));
					st.setCreatedAt(rs.getString("created_at"));
					st.setModifiedBy(rs.getInt("modified_by"));
					st.setModifiedAt(rs.getString("modified_by"));
					return st;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
