package com.dao.mangoplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;
import com.dao.mangoplate.MyConnection;
import com.dto.mangoplate.User;

public class userController {
	private User user;
	static Scanner sc = new Scanner(System.in);

	static String userID;
	static String passwd;
	static String name;
	static String address;
	static String phone;
	static String email;
	static String birth;

	//
	static String verifiedID;
	//
	private static int user_type;
	static Connection conn = null;

	public userController() {

	}
	public userController(User user) {

	}

	//관리자 회원가입
	public static void admin_register() {
		System.out.println("ID를 입력하세요.");
		userID = sc.nextLine();
		while("".equals(userID)) {
			System.out.println("ID는 필수입니다. ID를 입력하세요.");
			userID = sc.nextLine();
		}

		System.out.println("비밀번호를 입력하세요");
		passwd = sc.nextLine();
		System.out.println("성명을 입력하세요.");
		name = sc.nextLine();
		System.out.println("주소를 입력하세요.");
		address = sc.nextLine();
		System.out.println("전화번호를 입력하세요.");
		phone = sc.nextLine();
		System.out.println("이메일을 입력하세요");
		email = sc.nextLine();
		System.out.println("생년월일을 입력하세요.YY/MM/DD");
		birth = sc.nextLine();

		User user = new User(userID, passwd, name, address, phone, email, birth, user_type);
		//관리자 유저타입 0
		user.setUser_type(0);
		userController controller = new userController();
		controller.insert(user);
	}

	//점주 회원가입
	public static void ceo_register() {

		System.out.println("ID를 입력하세요.");
		userID = sc.nextLine();
		while("".equals(userID)) {
			System.out.println("ID는 필수입니다. ID를 입력하세요.");
			userID = sc.nextLine();
		}

		System.out.println("비밀번호를 입력하세요");
		passwd = sc.nextLine();
		System.out.println("성명을 입력하세요.");
		name = sc.nextLine();
		System.out.println("주소를 입력하세요.");
		address = sc.nextLine();
		System.out.println("전화번호를 입력하세요.");
		phone = sc.nextLine();
		System.out.println("이메일을 입력하세요");
		email = sc.nextLine();
		System.out.println("생년월일을 입력하세요.YY/MM/DD");
		birth = sc.nextLine();

		User user = new User(userID, passwd, name, address, phone, email, birth, user_type);
		//점주 유저타입 1
		user.setUser_type(1);
		userController controller = new userController();
		controller.insert(user);
	}

	//일반고객 회원가입
	public static void user_register() {
		System.out.println("ID를 입력하세요.");
		userID = sc.nextLine();
		while("".equals(userID)) {
			System.out.println("ID는 필수입니다. ID를 입력하세요.");
			userID = sc.nextLine();
		}

		System.out.println("비밀번호를 입력하세요");
		passwd = sc.nextLine();

		System.out.println("성명을 입력하세요.");
		name = sc.nextLine();

		System.out.println("주소를 입력하세요.");
		address = sc.nextLine();

		System.out.println("전화번호를 입력하세요.");
		phone = sc.nextLine();

		System.out.println("이메일을 입력하세요");
		email = sc.nextLine();

		System.out.println("생년월일을 입력하세요.YY/MM/DD");
		birth = sc.nextLine();

		User user = new User(userID, passwd, name, address, phone, email, birth, user_type);
		user.setUser_type(2);
		//일반고객 유저타입 2
		userController controller = new userController();
		controller.insert(user);
	}

	public void insert(User user) {
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
		} catch (SQLException e) {
			System.out.println("구문입력 오류");
			System.out.println("회원가입 되지않고 처음으로 돌아갑니다.");
		} finally {
			MyConnection.close(null, pstmt, conn);
		}
	}

	//로그인
	public static void login() {
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
				System.out.println("로그인이 되었습니다");
				System.out.println("안녕하세요 " +user_name+"님 반갑습니다.");

				if (user_type == 1) {
					User.verifiedCeoID = user_ID; //로그인된 점주 아이디 저장
					ceo_menu();
				} else if (user_type == 2) {
					User.verifiedCustomerID = user_ID; //로그인된 사용자 아이디 저장
					cus_menu();
				}
			}

		} catch (SQLException e) {
			System.out.println("ID 혹은 비밀번호가 일치하지 않습니다");
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}			
	}

	//점주 페이지
	public static void ceo_menu() {
		System.out.println("점주님 음식점 관리 메뉴를 보여드릴게요!");
		System.out.println("1 : 음식점 승인 요청, 2 : 음식점 수정, 3 : 내 가게 조회, 4 : 음식점 철회 요청, 5 : 철회 요청 취소, 9 : 종료");
		String ceo_ch = sc.nextLine();
		shopController ceo = new shopController(); 

		if (ceo_ch.equals("1")) {
			//1. 음식점 승인 요청 메소드 호출
			ceo.ceo_page();

		} else if (ceo_ch.equals("2")) {
			//2. 음식점 수정 메소드 호출
			ceo.modify_shopInfo();

		} else if (ceo_ch.equals("3")) {
			//3. 내 가게 조회
			ceo.search_shop();
			userController.ceo_menu();

		} else if (ceo_ch.equals("4")) {
			//4. 음식점 철회 요청 메소드 호출
			ceo.revokeShop_Request(user_type);

		} else if (ceo_ch.equals("5")) {
			//5. 철회 요청 취소 메소드 호출
			ceo.revokeCancel_Request(user_type);
		}

	}

	//일반고객 페이지
	public static void cus_menu() {
		CusRepository CusRep = new CusRepository();
		System.out.println("망고플레이트에 오신걸 환영합니다!");
		System.out.println("원하시는 기능을 선택해주세요!");
		System.out.println("1 : 전체 음식점 조회, 2 : 음식점 검색, 3 : 내 리뷰 보기, 4 : 회원정보 수정");
		String cus_ch = sc.nextLine();

		if (cus_ch.equals("1")) {
			//1. 전체 음식점 조회 메소드 호출
			CusRep.cusAllShopList();

		} else if (cus_ch.equals("2")) {
			//2. 음식점 검색 메소드 호출
			CusRep.cusCategoryShopList();

		} else if (cus_ch.equals("3")) {
			//3. 내 리뷰 보기 메소드 호출
			ReviewController.readMyReview();

		} else if (cus_ch.equals("4")) {
			//4. 회원정보 수정 메소드 호출
			user_modify();
		}
	}


	//일반고객 회원정보 수정
	public static void user_modify() {

		System.out.println("새 비밀번호를 입력하세요");
		String passwd = sc.nextLine();

		System.out.println("새 성명을 입력하세요.");
		String name = sc.nextLine();

		System.out.println("새 주소를 입력하세요.");
		String address = sc.nextLine();

		System.out.println("새 전화번호를 입력하세요.");
		String phone = sc.nextLine();

		System.out.println("새 이메일을 입력하세요");
		String email = sc.nextLine();

		System.out.println("새 생년월일을 입력하세요.YY/MM/DD");
		String birth = sc.nextLine();

		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		PreparedStatement pstmt = null;
		try {
			String SQL =
					"UPDATE mango_user\r\n"
							+ "SET passwd = ?, name = ?, address = ?, phone = ?, email = ?, birth = ?\r\n"
							+ "WHERE userid = ?";

			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, passwd);
			pstmt.setString(2, name);
			pstmt.setString(3, address);
			pstmt.setString(4, phone);
			pstmt.setString(5, email);
			pstmt.setString(6, birth);
			pstmt.setString(7, User.verifiedCustomerID);
			pstmt.executeUpdate();
			System.out.println("수정되었습니다!");												
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(null, pstmt, conn);
		}
	}

}


