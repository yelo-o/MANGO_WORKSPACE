package com.repository.mangoplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

import com.dao.mangoplate.MyConnection;
import com.dao.mangoplate.UserController;
import com.dto.mangoplate.User;

public class UserRepository {
	static Scanner sc = new Scanner(System.in);

	static String userID;
	static String passwd;
	static String name;
	static String address;
	static String phone;
	static String email;
	static String birth;
	static AdminRepository admin =new AdminRepository();
	static UserController userCon = new UserController();
	//
	static String verifiedID;
	//
	static Connection conn = null;
	public static void login() throws ClassNotFoundException {
		String userID;
		String passwd;

		System.out.println("ID를 입력하세요");
		userID = sc.nextLine();
		System.out.println("비밀번호를 입력하세요");
		passwd = sc.nextLine();

		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String loginSQL =
				"SELECT name, user_type, userid, passwd\r\n"
						+ "FROM mango_user\r\n"
						+ "WHERE userid = ? AND passwd = ?";
		try {
			pstmt = conn.prepareStatement(loginSQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				throw new SQLException();
			} else {
				String user_name = rs.getString(1);
				int user_type = rs.getInt(2);
				String user_ID = rs.getString(3);
				System.out.println("안녕하세요 " +user_name+"님 반갑습니다.");

				if (user_type == 1) {
					User.verifiedCeoID = user_ID; //로그인된 점주 아이디 저장
					UserController.ceo_menu();
				} else if (user_type == 2) {
					User.verifiedCustomerID = user_ID; //로그인된 사용자 아이디 저장
					UserController.cus_menu();
				}else if(user_type == 0) {
					User.verifiedAdminID = user_ID;
					UserController.admin_menu();
				}
			}

		} catch (SQLException e) {
			System.out.println("ID 혹은 비밀번호가 일치하지 않습니다");
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}			
	}
	public static void register(int user_type) {

		int length;

		do {
			System.out.println("ID를 입력하세요. (최소 6자 이상 12 이하, 영문자 혹운 숫자 조합으로 이루어져야합니다.)");

			userID = sc.nextLine();
			length = userID.length();
			while("".equals(userID)) {
				System.out.println("ID는 필수입니다. ID를 입력하세요.");
				userID = sc.nextLine();
			}
		}
		while(!(length >= 6 && length <= 12 && userID.matches(".*[a-z|0-9]+.*") ));

		String re_passwd;
		do {
			do {
				System.out.println("비밀번호를 입력하세요. (최소 6자 이상 12 이하, 영문자와 숫자 조합으로 이루어져야합니다.)");

				passwd = sc.nextLine();
				length = passwd.length();
			} while(!(length >= 6 && length <= 12 &&
					passwd.matches(".*[a-z]+.*") &&
					passwd.matches(".*[0-9]+.*")
					));

			System.out.println("비밀번호를 재입력하세요.");
			re_passwd = sc.nextLine();

		} while(!(passwd.equals(re_passwd)));

		System.out.println("성명을 입력하세요.");
		name = sc.nextLine();

		System.out.println("주소를 입력하세요.");
		address = sc.nextLine();

		System.out.println("전화번호를 입력하세요.");
		phone = sc.nextLine();

		while(!(phone.matches("^\\d{3}-\\d{3,4}-\\d{4}$"))) {
			System.out.println("올바른 전화번호 형식이 아닙니다.");
			System.out.println("010-XXX-XXXX 혹은 010-XXXX-XXXX 형식으로 다시 입력해주세요.");
			phone = sc.nextLine();
		}

		System.out.println("이메일을 입력하세요");
		email = sc.nextLine();

		while(!(email.matches("^[a-zA-Z0-9_+&*-]+(?:\\." +
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$"))) {
			System.out.println("올바른 이메일 형식이 아닙니다. 이메일을 다시 입력해주세요.");
			email = sc.nextLine();
		}

		System.out.println("생년월일을 입력하세요.YYYYMMDD");
		birth = sc.nextLine();

<<<<<<< HEAD
		  while (!(birth.matches("\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|[3][01])"))) {
		         System.out.println("올바른 날짜 형식이 아닙니다. YYYYMMDD 형식으로 다시 입력해주세요.");
		         birth = sc.nextLine();            
=======
		while (!(birth.matches("\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|[3][01])"))) {
			System.out.println("올바른 날짜 형식이 아닙니다. YYYYMMDD 형식으로 다시 입력해주세요.");
			birth = sc.nextLine();         
>>>>>>> 9589d84030599ff498cec2be2ec984b6ac4b113d
		}

		User user = new User(userID, passwd, name, address, phone, email, birth, user_type);
		user.setUser_type(user_type);
		insert_user(user);
	}

	public static void insert_user(User user) {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		String registerSQL =
				"INSERT INTO mango_user (userid, passwd, name, address, phone, email, birth, user_type) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(registerSQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getPasswd());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getAddress());
			pstmt.setString(5, user.getPhone());
			pstmt.setString(6, user.getEmail());
			pstmt.setString(7, user.getBirth());
			pstmt.setInt(8, user.getUser_type());
			pstmt.executeUpdate();
			System.out.println("회원가입 성공!");

		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("이미 존재하는 ID입니다.");
			System.out.println("회원가입 되지않고 처음으로 돌아갑니다.");
			//			e.printStackTrace();
		} catch (SQLException e) {
				e.printStackTrace();
			System.out.println("날짜형식이 올바르지 않습니다.");
			System.out.println("회원가입 되지않고 처음으로 돌아갑니다.");
		} finally {
			MyConnection.close(null, pstmt, conn);
		}
	}
	//일반고객 회원정보 수정
	public static void user_modify() throws ClassNotFoundException, SQLException {

		System.out.println("수정하고싶으신 정보를 선택하세요.");
		System.out.println("1 : 비밀번호, 2 : 성명, 3 : 주소, 4 : 전화번호, 5 : 이메일, 6 : 생년월일");
		String ch = sc.nextLine();

		Connection conn = null;
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		if (ch.equals("1")) {
			System.out.println("새 비밀번호를 입력하세요");
			passwd = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET passwd = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, passwd);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("비밀번호가 수정되었습니다!");
				UserController.cus_menu();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}			
		} else if (ch.equals("2")) {
			System.out.println("새 성명을 입력하세요");
			name = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET name = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, name);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("성명이 수정되었습니다!");		
				UserController.cus_menu();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}		
		} else if (ch.equals("3")) {
			System.out.println("새 주소를 입력하세요");
			address = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET address = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, address);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("주소가 수정되었습니다!");	
				UserController.cus_menu();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}		
		} else if (ch.equals("4")) {
			System.out.println("새 전화번호를 입력하세요");
			phone = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET phone = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, phone);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("전화번호가 수정되었습니다!");	
				UserController.cus_menu();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}		
		} else if (ch.equals("5")) {
			System.out.println("새 이메일을 입력하세요");
			email = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET email = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, email);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("이메일이 수정되었습니다!");	
				UserController.cus_menu();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}		
		} else if (ch.equals("6")) {
			System.out.println("새 생년월일을 입력하세요");
			birth = sc.nextLine();
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET birth = ?\r\n"
							+ "WHERE userid = ?";
			PreparedStatement pstmt = null;

			try {
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, birth);
				pstmt.setString(2, User.verifiedCustomerID);
				pstmt.executeUpdate();
				System.out.println("생일이 수정되었습니다!");	
				UserController.cus_menu();
			} catch (SQLException e) {
				System.out.println("구문입력 오류");
				System.out.println("정보가 수정되지 않았습니다.");
				UserController.cus_menu();
				//				e.printStackTrace();
			} finally {
				MyConnection.close(null, pstmt, conn);
			}		
		}

	}
}
