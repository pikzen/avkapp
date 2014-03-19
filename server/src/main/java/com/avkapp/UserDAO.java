package com.avkapp;
import com.avkapp.DatabaseHelper;
import com.avkapp.User;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.security.MessageDigest;

public class UserDAO {
	static final String COL_ID = "Id";
	static final String COL_FIRSTNAME = "Firstname";
	static final String COL_LASTNAME = "Lastname";
	static final String COL_EMAIL = "Email";
	static final String COL_LOGIN = "Login";
	static final String COL_PHONE = "Phone";
	static final String COL_PASSWORD = "Password";
	static final String COL_PROFILE = "Profile";
	static final String COL_OFFICE = "Office";
	static final String COL_PIN = "PIN";


	public void insert(User i) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		PreparedStatement stmt = null;
		String query = "INSERT INTO Users(Firstname, Lastname, Email, Login, Phone, Password, PIN, Profile, Office) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
	
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);		
			stmt.setString(1, i.getFirstname());
			stmt.setString(2, i.getLastname());
			stmt.setString(3, i.getEmail());
			stmt.setString(4, i.getLogin());
			stmt.setString(5, i.getPhone());

			try {
				// Le mot de passe & le PIN sont stockés sous la forme d'un hash.
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(i.getPassword().getBytes("UTF-8"));
				String password = new String(md.digest());
				stmt.setString(6, password);

				md.update(i.getPin().getBytes("UTF-8"));
				String pin = new String(md.digest());
				stmt.setString(7, pin);
			}
			catch (Exception e) { // TODO : pokemon catching
				log.log(Level.WARNING, e.getMessage());
			}

			stmt.setString(8, i.getProfile());
			stmt.setString(9, i.getOffice());

		
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	public boolean loginExists(String login) throws SQLException {
		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users WHERE Login=?";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);		
			stmt.setString(1, login);		
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) return true;
		}
		catch (SQLException e) {
		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}

		return false;
	}
	public ArrayList<User> getAll() throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT Id, Firstname, Lastname, Email, Login, Phone, Password,PIN, Profile, Office FROM Users;";
		ArrayList<User> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			result = new ArrayList<User>();

			while (rs.next()) {
				User inter = new User(rs.getInt(COL_ID),
									  rs.getString(COL_FIRSTNAME),
								      rs.getString(COL_LASTNAME),
								      rs.getString(COL_EMAIL),
								      rs.getString(COL_LOGIN),
								      rs.getString(COL_PHONE),
								      rs.getString(COL_PASSWORD),
								      rs.getString(COL_PIN),
								      rs.getInt(COL_PROFILE),
								      rs.getInt(COL_OFFICE));
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

	public User getById(int id) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Id, Firstname, Lastname, Email, Login, Phone, Password, Profile, Office FROM Users " + 
					   "WHERE Id = ?;";
		User result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery(query);
		
			if (rs.next()) {
				result = new User(rs.getInt(COL_ID),
									  rs.getString(COL_FIRSTNAME),
								      rs.getString(COL_LASTNAME),
								      rs.getString(COL_EMAIL),
								      rs.getString(COL_LOGIN),
								      rs.getString(COL_PHONE),
								      rs.getString(COL_PASSWORD),
								      rs.getString(COL_PIN),
								      /*rs.getInt(COL_PROFILE),
								      rs.getInt(COL_OFFICE)*/
								      	"1",
								      	"1");
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
