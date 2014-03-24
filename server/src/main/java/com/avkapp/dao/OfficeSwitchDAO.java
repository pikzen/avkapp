package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.Encryption;
import com.avkapp.data.OfficeSwitch;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class OfficeSwitchDAO {
	static final String COL_USER = "UserId";
	static final String COL_OFFICE = "OfficeId";

	public void initDb() throws SQLException {
	}

	public ArrayList<OfficeSwitch> getAllOfficeSwitchs() throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT UserId, OfficeId FROM OfficeSwitch;";
		ArrayList<OfficeSwitch> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			result = new ArrayList<OfficeSwitch>();

			while (rs.next()) {
				OfficeSwitch inter = new OfficeSwitch(rs.getInt(COL_USER),
									  				  rs.getInt(COL_OFFICE));
				result.add(inter);
			}
		}
		catch (SQLException e) {
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
		return result;	
	}

	public ArrayList<OfficeSwitch> getOfficeSwitchByOfficeId(int id) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT UserId, OfficeId FROM OfficeSwitch WHERE OfficeId = ?;";
		ArrayList<OfficeSwitch> result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery(query);
		
			result = new ArrayList<OfficeSwitch>();

			while (rs.next()) {
				OfficeSwitch inter = new OfficeSwitch(rs.getInt(COL_USER),
									  				  rs.getInt(COL_OFFICE));
				result.add(inter);
			}
		}
		catch (SQLException e) {
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
		return result;	
	}
}