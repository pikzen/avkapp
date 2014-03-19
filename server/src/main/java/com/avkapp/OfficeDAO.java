package com.avkapp;
import com.avkapp.DatabaseHelper;
import com.avkapp.Office;
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
