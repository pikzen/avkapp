package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.Encryption;
import com.avkapp.data.INRTreatmentPhase;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class INRTreatmentPhaseDAO {
	static final String COL_ID = "Id";
	static final String COL_NAME = "Name";

	public void initDb() throws SQLException {
		//insert(new INRTreatmentPhase(0, "Initiation"));
		//insert(new INRTreatmentPhase(1, "Entretien"));
	}

	private void empty() throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "TRUNCATE TABLE INRTreatmentPhase;";

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		}
		catch (SQLException e) {
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	public INRTreatmentPhase getINRTreatmentPhaseById(int id) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT * FROM INRTreatmentPhase WHERE Id = ?";
		INRTreatmentPhase result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
		

			if (rs.next()) {
				result = new INRTreatmentPhase(rs.getInt(COL_ID),
									  	       rs.getString(COL_NAME));
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