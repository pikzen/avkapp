package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.data.Profile;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProfileDAO {
	static final String COL_ID = "Id";
	static final String COL_NAME = "Name";

	public void initDb() throws SQLException {
		insert(new Profile(Profile.ROLE_ADMIN, "Administrateur"));
		insert(new Profile(Profile.ROLE_RESPONSABLE, "Responsable de cabinet"));
		insert(new Profile(Profile.ROLE_MEDECIN, "Médecin"));
		insert(new Profile(Profile.ROLE_INFIRMIER, "Infirmier"));
		insert(new Profile(Profile.ROLE_AUTOMED, "Patient en automédication"));
	}

	public void insert(Profile i) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		PreparedStatement stmt = null;
		String query = "INSERT INTO Profile(Name) VALUES(?);";


		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, i.getName());

			stmt.executeUpdate();
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}

  public String getProfileAsText(int prof) throws SQLException {
    PreparedStatement stmt = null;
		String query = "SELECT Name FROM Profile WHERE Id=?";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

    stmt = conn.prepareStatement(query);
    stmt.setInt(1, prof);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) return rs.getString(COL_NAME);

    return "";
  }

	public Profile getById(String id) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Id, Name FROM Profile WHERE Id = ?;";
		Profile result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);

			ResultSet rs =  stmt.executeQuery();

			if (rs.next()) {
				result = new Profile(rs.getInt(COL_ID), rs.getString(COL_NAME));
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
	public ArrayList<Profile> getAll() throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT Id, Name FROM Profile;";
		ArrayList<Profile> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			result = new ArrayList<Profile>();

			while (rs.next()) {
				Profile inter = new Profile(rs.getInt(COL_ID),
										  rs.getString(COL_NAME));
				result.add(inter);
			}
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
		return result;
	}
	public ArrayList<Profile> getAllPublic() throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		// On ne renvoie pas l'administrateur, pour des raisons évidentes
		String query = "SELECT * FROM Profile WHERE Id != 1;";
		ArrayList<Profile> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			result = new ArrayList<Profile>();

			while (rs.next()) {
				Profile inter = new Profile(rs.getInt(COL_ID),
										  rs.getString(COL_NAME));
				result.add(inter);
			}
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
		return result;
	}
}
