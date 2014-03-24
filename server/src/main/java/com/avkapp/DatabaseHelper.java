package com.avkapp;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.annotation.Resource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseHelper {

	private String UserName = "root";
	private String Password = "rootpassword";
	private String Host     = "localhost";
	private String Database = "avkapp";
	
	private MysqlDataSource ds;

	public void initDb() throws SQLException {
		query("DROP DATABASE IF EXISTS " + Database + "; CREATE DATABASE " + Database +";GRANT USAGE ON " + Database + ".* to "+ UserName + "@" + Host + " IDENTIFIED BY '" + Password + "';");
	}

	public void query(String query) throws SQLException{
		Connection conn = getConnection();
		Statement stmt = null;

		stmt = conn.createStatement();
		stmt.executeQuery(query);

		if (conn != null) conn.close();
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public DatabaseHelper() {
		ds = new MysqlDataSource();
		ds.setUser(this.UserName);
		ds.setPassword(this.Password);
		ds.setServerName(this.Host);
		ds.setDatabaseName(this.Database);
	}
}
