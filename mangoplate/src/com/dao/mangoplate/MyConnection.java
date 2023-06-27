package com.dao.mangoplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection {
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn = null;
		String url = "jdbc:oracle:thin:@192.168.0.30:1521/xe";
		String user = "kosta";
		String password = "kosta";
		
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
