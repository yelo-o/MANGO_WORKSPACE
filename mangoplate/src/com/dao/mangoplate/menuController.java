package com.dao.mangoplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//클래스 메뉴컨트롤러
public class MenuController {


	private static BufferedReader reader; // 전역 변수로 BufferedReader 선언
	static Scanner sc = new Scanner(System.in);

	static Connection conn = null;
	static ResultSet rs = null;
	static PreparedStatement psmt = null;

	//메뉴추가
	public static void addMenu(String ceo_id, int shop_no) {

		System.out.println("메뉴 등록을 시작합니다.");
		int shopNo = shop_no;
		String Maxsql = "SELECT MAX(MENU_NO) FROM MENU";

		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 받아오는거
		
		int menuNo = 0;
		
		try {
			psmt = conn.prepareStatement(Maxsql);
			rs = psmt.executeQuery(); // 보내는거 + 초기화
			while(rs.next()) {
				int menunum = rs.getInt(1);
				menunum++;
				menuNo = menunum;
			}
			
//			MyConnection.close(rs, psmt, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 이렇게해야널포인트예외ㅇ안뜬다

		System.out.println("MENU_NAME을 입력하세요: ");
		String menuName = sc.nextLine();
		System.out.println("MENU_CONTENT를 입력하세요: ");
		String menuContent = sc.nextLine();

		//menu state 무조건1이되도록 // 메뉴슈정에서 0으로 만들어 판매중지되도록ㅁ낟르기 
		int menuState = 0;

		String sql = "INSERT INTO MENU (SHOP_NO, MENU_NO, MENU_NAME, MENU_CONTENT, MENU_STATE) "
				+ "VALUES (?, ?, ?, ?, ?)";

		Connection conn;
		try {
			conn = MyConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setInt(1, shopNo);
			stmt.setInt(2, menuNo);
			stmt.setString(3, menuName);
			stmt.setString(4, menuContent);
			stmt.setInt(5, menuState);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("메뉴가 성공적으로 등록되었습니다.");
			} else {
				System.out.println("메뉴 등록에 실패했습니다.");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	//모든 메뉴 조회
	public static void getAllMenus() {
		
		//DB연결 준비
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			// where 샵 스테이트 1인 애만 검색)
			String sql = "SELECT *\r\n"
					+ "FROM SHOP JOIN MENU ON(shop.shop_no=menu.shop_no)\r\n"
					+ "WHERE shop_state=1";
			psmt = conn.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			System.out.println("전체 메뉴 목록:");
			System.out.println("------------------------------------------------------");
			System.out.println("SHOP_NO\tMENU_NO\tMENU_NAME\tMENU_CONTENT\tMENU_STATE");
			System.out.println("------------------------------------------------------");
			
			while (rs.next()) {
				int shopNo = rs.getInt("SHOP_NO");
				int menuNo = rs.getInt("MENU_NO");
				String menuName = rs.getString("MENU_NAME");
				String menuContent = rs.getString("MENU_CONTENT");
				boolean menuState = rs.getBoolean("MENU_STATE");
				
//				System.out.printf("%i\t%i\t%s\t%s\t%s%n", shopNo, menuNo, menuName, menuContent, menuState);
				System.out.println(shopNo +  menuNo + menuName + menuContent + menuState);
//				System.out.println(menuNo + menuName + menuContent + menuState);
				
				System.out.println("------------------------------------------------------");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//샵 검색 - 메뉴 조회  
	public static void getMenusByShop(String shopName) {
		try (Connection conn = MyConnection.getConnection()) {
			String sql = "SELECT * \r\n"
					+ "FROM SHOP JOIN MENU ON(shop.shop_no=menu.shop_no) \r\n"
					+ "WHERE shop.shop_name= ?";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, shopName);
				ResultSet rs = stmt.executeQuery();

				System.out.println("해당 상점의 메뉴 목록:");
				System.out.println("------------------------------------------------------");
				System.out.println("MENU_NO\tMENU_NAME\tMENU_CONTENT\tMENU_STATE");
				System.out.println("------------------------------------------------------");

				while (rs.next()) {
					String menuNo = rs.getString("MENU_NO");
					String menuName = rs.getString("MENU_NAME");
					String menuContent = rs.getString("MENU_CONTENT");
					boolean menuState = rs.getBoolean("MENU_STATE");

					System.out.println( menuNo + menuName + menuContent + menuState);
				}

				System.out.println("------------------------------------------------------");

				rs.close();
			} catch (SQLException e) {
				System.out.println("메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
			}

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("데이터베이스 연결 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	public static void updateMenu() {
		//샵넘버 메뉴넘버빼기 기본과정만 
		// menu control [list써서 1234어쩌고 메뉴수정 와일문써서 ]

		System.out.println("메뉴 수정을 시작합니다.");

		System.out.println("SHOP_NO를 입력하세요: ");
		String searchShopNo = sc.nextLine();

		System.out.println("MENU_NO를 입력하세요: ");
		String searchMenuNo = sc.nextLine();

		Connection conn;
		try {
			conn = MyConnection.getConnection();

			String sql = "SELECT * FROM MENU WHERE SHOP_NO = ? AND MENU_NO = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, searchShopNo);
			stmt.setString(2, searchMenuNo);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println("메뉴 정보:");
				System.out.println("SHOP_NO: " + rs.getString("SHOP_NO"));
				System.out.println("MENU_NO: " + rs.getString("MENU_NO"));
				System.out.println("MENU_NAME: " + rs.getString("MENU_NAME"));
				System.out.println("MENU_CONTENT: " + rs.getString("MENU_CONTENT"));
				System.out.println("MENU_STATE: " + rs.getBoolean("MENU_STATE"));

				System.out.println("수정할 내용을 입력하세요.");
				System.out.println("수정할 MENU_NAME을 입력하세요: ");
				String updatedMenuName = sc.nextLine();

				sql = "UPDATE MENU SET MENU_NAME = ? WHERE SHOP_NO = ? AND MENU_NO = ?";

				PreparedStatement updateStmt = conn.prepareStatement(sql);
				updateStmt.setString(1, updatedMenuName);
				updateStmt.setString(2, searchShopNo);
				updateStmt.setString(3, searchMenuNo);
				int rowsAffected = updateStmt.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("메뉴 이름이 변경되었습니다. 변경된 메뉴 이름: " + updatedMenuName);
				} else {
					System.out.println("메뉴 수정에 실패했습니다.");
				}

			} else {
				System.out.println("일치하는 메뉴가 없습니다.");
			}

			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteMenu() {
		System.out.println("메뉴 삭제를 시작합니다.");

		System.out.println("SHOP_NO를 입력하세요: ");
		String searchShopNo = sc.nextLine();

		System.out.println("MENU_NO를 입력하세요: ");
		String searchMenuNo = sc.nextLine();

		Connection conn;
		try {
			conn = MyConnection.getConnection();

			String sql = "SELECT * FROM MENU WHERE SHOP_NO = ? AND MENU_NO = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, searchShopNo);
			stmt.setString(2, searchMenuNo);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println("메뉴 정보:");
				System.out.println("SHOP_NO: " + rs.getString("SHOP_NO"));
				System.out.println("MENU_NO: " + rs.getString("MENU_NO"));
				System.out.println("MENU_NAME: " + rs.getString("MENU_NAME"));
				System.out.println("MENU_CONTENT: " + rs.getString("MENU_CONTENT"));
				System.out.println("MENU_STATE: " + rs.getBoolean("MENU_STATE"));

				System.out.println("정말로 메뉴를 삭제하시겠습니까? (Y/N)");
				String confirmation = sc.nextLine();

				if (confirmation.equalsIgnoreCase("Y")) {
					sql = "DELETE FROM MENU WHERE SHOP_NO = ? AND MENU_NO = ?";

					PreparedStatement deleteStmt = conn.prepareStatement(sql);
					deleteStmt.setString(1, searchShopNo);
					deleteStmt.setString(2, searchMenuNo);
					int rowsAffected = deleteStmt.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println("메뉴가 삭제되었습니다.");
					} else {
						System.out.println("메뉴 삭제에 실패했습니다.");
					}
				}
			} else {
				System.out.println("메뉴 삭제가 취소되었습니다.");
			}

			rs.close();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void exitProgram() {
		System.out.println("프로그램을 종료합니다.");
		System.exit(0);
	}
	/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		reader = new BufferedReader(new InputStreamReader(System.in)); // BufferedReader 초기화

		try {
			while (true) {
				System.out.println("1. 메뉴 등록"); // 관리자
				System.out.println("2. 전체메뉴조회"); // 관리자 사용자
				System.out.println("3. Shop별 메뉴조회"); // 관리자 사용자
				System.out.println("4. 메뉴 수정"); // 관리자
				System.out.println("5. 메뉴 삭제");
				System.out.println("6. 종료");
				System.out.println("번호를 선택하세요: ");
				String choice = sc.nextLine();

				if (choice.equals("1")) {
					addMenu();
				} else if (choice.equals("2")) {
					getAllMenus();
				} else if (choice.equals("3")) {
					System.out.println("상점 번호를 입력하세요: ");
					String shopNo = sc.nextLine();
					getMenusByShop(shopNo);
				} else if (choice.equals("4")) {
					updateMenu();
				} else if (choice.equals("5")) {
					deleteMenu();
				} else if (choice.equals("6")) {
					exitProgram();
				} else {
					System.out.println("유효한 번호를 선택하세요.");
				}
			}

		} finally {
			try {
				reader.close(); // BufferedReader 닫기
			} catch (IOException e) {
				System.out.println("입력 스트림을 닫는 중 오류가 발생했습니다: " + e.getMessage());
			}
		}
	}
	*/
}
