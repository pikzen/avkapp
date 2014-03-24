package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.Encryption;
import com.avkapp.data.Note;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class NoteDAO {
	static final String COL_ID = "Id";
	static final String COL_DATE = "Date";
	static final String COL_NOTE = "Note";

	public void initDb() throws SQLException {
	}

	public ArrayList<Note> getPatientNotes(int patientId) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT Note.Id, Note.Date, Note.Note FROM Note, PatientNotes " + 
					   "WHERE PatientNotes.Patient = ? " + 
					   "AND PatientNotes.Note = Note.Id;";
		ArrayList<Note> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			result = new ArrayList<Note>();

			while (rs.next()) {
				Note inter = new Note(rs.getInt(COL_ID),
									  rs.getDate(COL_DATE),
									  rs.getString(COL_NOTE));
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