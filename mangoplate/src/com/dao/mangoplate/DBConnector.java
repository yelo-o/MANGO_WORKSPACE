package com.dao.mangoplate;
import java.sql.*;


public class DBConnector {

	public static Connection getConnect(){
		//드라이버 로드

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;

		try {
			Class.forName("oracle.jdbc.driver.Orac1leDriver");
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String id = "localmg";
			String pw = "localmg";
			System.out.println("DB정상연결");
			try {
				con = DriverManager.getConnection(url,id,pw);
				System.out.println("DB계정일치");
			} catch (SQLException e) {
				System.out.println("DB계정불일치");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("DB연결실패");
		}
		return con;
	}
	
	public static void close(ResultSet rs, PreparedStatement psmt, Connection con) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}if(psmt!=null) {
			try {
				psmt.close();
			} catch (SQLException e) {
			}
		}
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}
}

