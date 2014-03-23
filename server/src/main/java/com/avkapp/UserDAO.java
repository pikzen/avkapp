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
				stmt.setString(6, Encryption.SHA256(i.getPassword()));
				stmt.setString(7, Encryption.SHA256(i.getPin()));
			}
			catch (Exception e) { // TODO : pokemon catching
				log.log(Level.WARNING, e.getMessage());
			}

			stmt.setInt(8, i.getProfile());
			stmt.setInt(9, i.getOffice());

		
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

	public boolean emailExists(String email) throws SQLException {
		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users WHERE Email=?;";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);		
			stmt.setString(1, email);		
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

	public boolean loginExists(String login) throws SQLException {
		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users WHERE Login=?;";

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
	/**
	* Returns the list of users not yet validated by the administrator
	* @param info The user information (login, password, pin)
	* @return The list of users that his user can validate, or null if he isn't authorized
	*/
	public ArrayList<User> getUnvalidatedUsers(LoginInfo info) throws SQLException {
		// Check if the user is authorized to list it
		if (isAuthorizedToListUsers(info)) {
			ArrayList<User> users = getAll(info);

			return users;
		}
		else {
			return null;
		}
	}


	private int getRole(LoginInfo info) throws SQLException {

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Profile FROM Users WHERE Login = ? AND Password = ?;";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, info.getUsername());
			stmt.setString(2, Encryption.SHA256(info.getPassword()));

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(COL_PROFILE);
			}
		}
		catch (SQLException e) {

		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}

		return -1;
	}

	/**
	* Returns the list of users that this person can list
	* Administrator can list everyone, Responsable can list those who are in his office
	* @param info The user information
	* @return The list of users, or null if the user is not allowed to list
	*/
	private ArrayList<User> listableUsers(LoginInfo info) throws SQLException {
		PreparedStatement stmt = null;
		int userRole = getRole(info);

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		String query = "";

		if (userRole == -1) {
			return null;
		}
		else if (userRole == Profile.ROLE_ADMIN) {
			// MySQL utilise un TINYINT pour représenter un BOOLEAN. 0 = false, x = true
			query = "SELECT * FROM Users WHERE Users.Validated = 0;";
		}
		else if (userRole == Profile.ROLE_RESPONSABLE) {
			query = "SELECT * FROM Users" + 
					 "WHERE Users.Validated = 0" + 
					 "AND Users.Office IN (" + 
					 	"SELECT Office.Id FROM Office, Users" + 
					 	"WHERE Office.Id = Users.Office" + 
					 	"AND Users.Login = ?" + 
					 	"AND Users.Password = ?" +
					 ");";
		}
		else {
			return null;
		}

		return null;
	}

	public User getByLoginInfo(LoginInfo log) throws SQLException {

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users WHERE Login = ? AND Password = ?;";
		User result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, log.getUsername());
			stmt.setString(2, Encryption.SHA256(log.getPassword()));

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				result = new User(rs.getInt(COL_ID),
								  rs.getString(COL_FIRSTNAME),
							      rs.getString(COL_LASTNAME),
							      rs.getString(COL_EMAIL),
							      rs.getString(COL_LOGIN),
							      rs.getString(COL_PHONE),
							      rs.getString(COL_PASSWORD),
							      rs.getString(COL_PIN),
							      rs.getInt(COL_PROFILE),
							      rs.getInt(COL_OFFICE));
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

	public boolean validate(LoginInfo log) throws SQLException {

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users WHERE Login = ? AND Password = ?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, log.getUsername());
			stmt.setString(2, Encryption.SHA256(log.getPassword()));

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {

		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}

		return false;
	}

	private boolean isAuthorizedToListUsers(LoginInfo info) throws SQLException {
		if (info == null || info.getUsername() == null || info.getPassword() == null) {
			return false;
		}

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Profile FROM Users WHERE Login = ? AND Password = ?;";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, info.getUsername());
			stmt.setString(2, Encryption.SHA256(info.getPassword()));

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int profile = rs.getInt(COL_PROFILE);

				if (profile == Profile.ROLE_ADMIN ||
					profile == Profile.ROLE_RESPONSABLE) {
					return true;
				}
			}
		}
		catch (SQLException e) {

		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}

		return false;
	}


	/**
	* Returns the list of all users
	* @param info The asking user information (login, password, pin)
	* @return The list of all users, or null if he isn't authorized
	*/
	public ArrayList<User> getAll(LoginInfo info) throws SQLException {
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
								      rs.getInt(COL_PROFILE),
								      rs.getInt(COL_OFFICE));
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
