package com.avkapp.dao;
import com.avkapp.DatabaseHelper;
import com.avkapp.data.Office;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OfficeDAO {
	static final String COL_ID = "Id";
	static final String COL_NAME = "Name";
	static final String COL_ADDRESS = "Address";
	static final String COL_PHONE = "PhoneNumber";

	static final int NO_OFFICE = 0;
	static final int CREATE_OFFICE = 1;

	public void initDb() throws SQLException {
		insert(new Office(NO_OFFICE,
						  "Aucun cabinet",
						  "no-address",
						  "no-phone"));
		insert(new Office(CREATE_OFFICE,
			              "Je créérai mon cabinet",
						  "no-address",
						  "no-phone"));
	}
	public void acceptOffice(int id) throws SQLException {
		PreparedStatement stmt = null;
		String query = "UPDATE Office " +
					   "SET Validated = 1 " +
					   "WHERE Id = ?;";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);

			stmt.executeQuery();
		}
		catch (SQLException e) {

		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

  public String getOfficeAsText(int off) throws SQLException {
    PreparedStatement stmt = null;
		String query = "SELECT Name FROM Office WHERE Id=?;";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

    stmt = conn.prepareStatement(query);
    stmt.setInt(1, off);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) return rs.getString(COL_NAME);

    return "";
  }

	public void insert(Office i) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		PreparedStatement stmt = null;
		String query = "INSERT INTO Office(Name, Address, PhoneNumber) VALUES( ?, ?, ?);";


		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, i.getName());
			stmt.setString(2, i.getAddress());
			stmt.setString(3, i.getPhone());

			stmt.executeUpdate();
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}
	public Office getById(String id) throws SQLException {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Id, Name, Address, PhoneNumber FROM Office WHERE Id = ?;";
		Office result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);

			ResultSet rs =  stmt.executeQuery();

			if (rs.next()) {
				result = new Office(rs.getInt(COL_ID),
										  rs.getString(COL_NAME),
								    	  rs.getString(COL_ADDRESS),
								    	  rs.getString(COL_PHONE));
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
	public ArrayList<Office> getAll() throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT Id, Name, Address, PhoneNumber FROM Office;";
		ArrayList<Office> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			result = new ArrayList<Office>();

			while (rs.next()) {
				Office inter = new Office(rs.getInt(COL_ID),
										  rs.getString(COL_NAME),
								    	  rs.getString(COL_ADDRESS),
								    	  rs.getString(COL_PHONE));
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
