package com.avkapp;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.annotation.Resource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseHelper {

	private String UserName = "root";
	private String Password = "rootpassword";
	private String Host     = "localhost";
	private String Database = "avkapp";
	
	private MysqlDataSource ds;

	public Connection getConnection() throws SQLException {
		ds = new MysqlDataSource();
		ds.setUser(this.UserName);
		ds.setPassword(this.Password);
		ds.setServerName(this.Host);
		ds.setDatabaseName(this.Database);
		return ds.getConnection();
	}
}
