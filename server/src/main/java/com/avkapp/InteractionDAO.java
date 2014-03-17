package com.avkapp;
import com.avkapp.DatabaseHelper;
import com.avkapp.Interaction;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;

public class InteractionDAO {
	
	public void insert(Interaction i) throws SQLException {
		Logger log = Logger.getLogger("AVKApp");
		PreparedStatement stmt = null;
		String query = "INSERT INTO Medication(Name, INRImpact, Note)"+ 
			       "VALUES( ?, ?, ?);";
		
	
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();
		try {
			stmt = conn.prepareStatement(query);		
			stmt.setString(1, i.getName());
			stmt.setInt(2, i.getEffect());
			stmt.setString(3, i.getNote());
		
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}
	public ArrayList<Interaction> getAll() throws SQLException {
		Logger log = Logger.getLogger("AVKApp");

		DatabaseHelper db = new DatabaseHelper();
		Connection conn = db.getConnection();

		Statement stmt = null;
		String query = "SELECT Name, INRImpact, Note" + 
			       " FROM Medication;";
		ArrayList<Interaction> result = null;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		
			result = new ArrayList<Interaction>();

			while (rs.next()) {
				log.log(Level.INFO, "Query has elements");
				Interaction inter = new Interaction(rs.getString("Name"),
								    rs.getInt("INRImpact"),
								    rs.getString("Note"));
				result.add(inter);
			}
		}
		catch (SQLException e) {
			log.log(Level.WARNING, e.getMessage());
		}
		finally {
			if (stmt != null) stmt.close();
		}
		return result;	
	}
}
