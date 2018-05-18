package com.jmartinez.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/test.auto"; 

	// Database credentials
	static final String USER = "root";
	static final String PASS = "sistemas";

	public Connection getConexion() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {

		}
		return conn;
	}

}
