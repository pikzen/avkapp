package com.avkapp.dao;

import com.avkapp.DatabaseHelper;
import com.avkapp.Encryption;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.avkapp.data.User;
import com.avkapp.data.Profile;
import com.avkapp.data.Office;
import com.avkapp.data.LoginInfo;

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
	static final String COL_VALIDATED = "Validated";

	public void initDb(String adminPassword, String adminEmail) throws SQLException {
		insert(new User(0,
						"Administrateur",
						"Administrateur",
						adminEmail,
						"admin",
						"no-phone",
						adminPassword,
						"no-pin",
						Profile.ROLE_ADMIN,
						Office.NO_OFFICE,
						1));
	}

	/**
	* Valide l'utilisateur
	* @param id L'Id de l'utilisateur
	*/
	public void acceptUser(int id) throws SQLException {
		PreparedStatement stmt = null;
		String query = "UPDATE Users " +
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

	public boolean validate(LoginInfo log) {
		return authorize(0, log.getUsername(), log.getPassword());
	}

	public ArrayList<User> getWaiting(LoginInfo log) throws SQLException {
		String shaPass = Encryption.SHA256(log.getPassword());

		PreparedStatement stmt = null;
		int userRole = this.getRole(log);

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		String query = "";
		ArrayList<User> result = null;

		if (userRole == Profile.ROLE_ADMIN) {
			// MySQL utilise un TINYINT pour représenter un BOOLEAN. 0 = false, x = true
			query = "SELECT * FROM Users WHERE Validated = 0;";
			try {
				stmt = conn.prepareStatement(query);
			}
			catch (SQLException e) {

			}
		}
		else if (userRole == Profile.ROLE_RESPONSABLE) {
			query = "SELECT * FROM Users " +
					 "WHERE Users.Validated = 0 " +
					 "AND Users.Office IN ( " +
					 	"SELECT Office.Id FROM Office, Users " +
					 	"WHERE Office.Id = Users.Office " +
					 	"AND Users.Login = ? " +
					 	"AND Users.Password = ? " +
					 	"AND Users.Validated = 0" + 
					 ");";
			try {
				stmt = conn.prepareStatement(query);
				stmt.setString(1, log.getUsername());
				stmt.setString(2, shaPass);
			}
			catch (SQLException e) {

			}
		}
		else {
			return null;
		}


		try {

			ResultSet rs = stmt.executeQuery();

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
								      rs.getInt(COL_OFFICE),
								      rs.getInt(COL_VALIDATED));
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

	public boolean authorize(int required, String user, String password) {
		String shaPass = Encryption.SHA256(password);


		PreparedStatement stmt = null;
		String query = "SELECT Profile FROM Users WHERE Login = ? AND Password = ? AND Validated = 1";

		Connection conn = null;

		Logger.getLogger("AVKApp").log(Level.WARNING, user + "@" + password);

		try {
			DatabaseHelper db = new DatabaseHelper();
			conn = db.getConnection();

			stmt = conn.prepareStatement(query);
			stmt.setString(1, user);
			stmt.setString(2, shaPass);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int rights = Profile.ROLES.get(rs.getInt(COL_PROFILE));

				if ((rights & required) == required) {
					Logger.getLogger("AVKApp").log(Level.WARNING, "OK");
					return true;
				}
				else {
					// Droits insuffisants
					Logger.getLogger("AVKApp").log(Level.WARNING, "Droits insuffisants");
					return false;
				}
			}
			else {
				// Utilisateur non existant
				Logger.getLogger("AVKApp").log(Level.WARNING, "Utilisateur non existant");
				return false;
			}
		}
		catch(SQLException e) {
			Logger.getLogger("AVKApp").log(Level.WARNING, "SQLException");
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException e) {
				// Can't really do much here
			}
		}
		// Par défaut, on considère que la personne n'est pas authorisée
		return false;
	}

	/**
	* Met à jour le cabinet d'un utilisateur
	* @param id L'Id de l'utilisateur
	* @param newOffice L'id du nouveau cabinet
	*/
	public void updateOffice(int id, int newOffice) throws SQLException {
		PreparedStatement stmt = null;
		String query = "UPDATE Users " +
					   "SET Office = ? " +
					   "WHERE Id = ?;";

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, newOffice);
			stmt.setInt(2, id);

			stmt.executeQuery();
		}
		catch (SQLException e) {

		}
		finally {
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	/**
	* Insère un utilisateur
	* @param i Les données de cet utilisateur
	*/
	public void insert(User i) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		PreparedStatement stmt = null;
		String query = "INSERT INTO Users(Firstname, Lastname, Email, Login, Phone, Password, PIN, Profile, Office, Validated) " +
					   "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


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
			stmt.setInt(10, i.getValidated());


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

	/**
	* Vérifie si un email est déjà présent dans la liste des utilisateurs
	* @param email L'email à verifier
	*/
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

	/**
	* Vérifie si un login existe déjà dans la table
	* @param login Le login à vérifier
	*/
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
	* Renvoie le rôle d'un utilisateur
	* @param info L'utilisateur à vérifier
	*/
	public int getRole(LoginInfo info) throws SQLException {

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
	private int getOffice(LoginInfo info) throws SQLException {

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT Office FROM Users WHERE Login = ? AND Password = ?;";
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, info.getUsername());
			stmt.setString(2, Encryption.SHA256(info.getPassword()));

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(COL_OFFICE);
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
	public ArrayList<User> listableUsers(LoginInfo info) throws SQLException {
		String shaPass = Encryption.SHA256(info.getPassword());

		PreparedStatement stmt = null;
		int userRole = getRole(info);
		int office = getOffice(info);

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		String query = "";
		ArrayList<User> result = null;

		if (userRole == Profile.ROLE_ADMIN) {
			// MySQL utilise un TINYINT pour représenter un BOOLEAN. 0 = false, x = true
			query = "SELECT * FROM Users;";
			try {
				stmt = conn.prepareStatement(query);
			}
			catch (SQLException e) {

			}
		}
		else if (userRole == Profile.ROLE_RESPONSABLE) {
			query = "SELECT * FROM Users" +
					 "WHERE Users.Office IN (" +
					 	"SELECT Office.Id FROM Office, Users" +
					 	"WHERE Office.Id = Users.Office" +
					 	"AND Office.Id = ?" + 
					 	"AND Users.Login = ?" +
					 	"AND Users.Password = ?" +
					 ");";
			try {
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, office);
				stmt.setString(2, info.getUsername());
				stmt.setString(3, shaPass);
			}
			catch (SQLException e) {
				
			}
		}
		else {
			return null;
		}

		try {
			ResultSet rs = stmt.executeQuery();

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
								      rs.getInt(COL_OFFICE),
								      rs.getInt(COL_VALIDATED));
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
								      rs.getInt(COL_OFFICE),
								      rs.getInt(COL_VALIDATED));
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
		String query = "SELECT * FROM Users " +
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
								      rs.getInt(COL_OFFICE),
								      rs.getInt(COL_VALIDATED));
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

  public User getUser(LoginInfo info) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		PreparedStatement stmt = null;
		String query = "SELECT * FROM Users " +
					   "WHERE Login = ? AND Password = ?;";
		User result = null;

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, info.getUsername());
      stmt.setString(2, Encryption.SHA256(info.getPassword()));

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
								      rs.getInt(COL_OFFICE),
								      rs.getInt(COL_VALIDATED));
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
