package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.Encryption;
import com.avkapp.data.INRMedication;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class INRMedicationDAO {
	static final String COL_ID = "Id";
	static final String COL_NAME = "Name";

	public void initDb() throws SQLException {
		empty();
		//insert(new INRMedication(0, "Previscan"));
		//insert(new INRMedication(1, "Coumadine"));
	}

	private void empty() throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "TRUNCATE TABLE INRMedication;";

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

	public INRMedication getINRMedicationById(int id) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT * FROM INRMedication WHERE Id = ?";
		INRMedication result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
		

			if (rs.next()) {
				result = new INRMedication(rs.getInt(COL_ID),
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