package com.dao.mangoplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dto.mangoplate.Menu;

//클래스 메뉴컨트롤러
public class MenuController {


	static Scanner sc = new Scanner(System.in);
	static List<Menu>menu_list = new ArrayList<Menu>();
	

	//메뉴추가
	public static void addMenu(int shop_no) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		
		System.out.println("메뉴를 등록합니다.");
		int shopNo = shop_no;
		String Maxsql = "SELECT MAX(MENU_NO) FROM MENU";

		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, psmt, conn);
		}

		System.out.println("메뉴 이름을 입력하세요: ");
		String menuName = sc.nextLine();
		System.out.println("메뉴 설명을 입력하세요: ");
		String menuContent = sc.nextLine();


		String sql = "INSERT INTO MENU (SHOP_NO, MENU_NO, MENU_NAME, MENU_CONTENT, MENU_STATE) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try {
			conn = MyConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setInt(1, shopNo);
			stmt.setInt(2, menuNo);
			stmt.setString(3, menuName);
			stmt.setString(4, menuContent);
			stmt.setInt(5, 1);
			//menu state 무조건1이되도록 // 메뉴슈정에서 0으로 만들어 판매중지되도록ㅁ낟르기 

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("메뉴가 성공적으로 등록되었습니다.");
			} else {
				System.out.println("메뉴 등록에 실패했습니다.");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, psmt, conn);
		}
	}

	//모든 메뉴 조회
	public static void getAllMenus(int shop_no) throws ClassNotFoundException, SQLException {
		int counter=1;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;

		conn = MyConnection.getConnection();
		
		String search_menu = "select * from menu where shop_no = '"+shop_no+"'";
			
			try {
				psmt = conn.prepareStatement(search_menu);
				rs= psmt.executeQuery();
				System.out.println("--------------------------------------------------");
				while(true){
					if(rs==null) {
						System.out.println("등록된 메뉴정보가 없습니다.");
					}
					rs.next();
					int menu_no = rs.getInt(2);
					String menu_name = rs.getString(3);
					String menu_content = rs.getString(4);
					int menu_state = rs.getInt(5);
					Menu menu = new Menu(shop_no,menu_no,menu_name,menu_content,menu_state);
					menu_list.add(menu);
					if(menu_state == 1) {
					System.out.println(counter+"번 메뉴 : "+menu_name+"      판매중");
					counter++;}
					else if(menu_state == 0) {
						System.out.println(counter+"번 메뉴 : "+menu_name+"      판매중지");
						counter++;
					}

				}
			} catch (SQLException e) {
			}finally {
				System.out.println("--------------------------------------------------");
				MyConnection.close(rs,psmt,conn);
			}
	}
	//샵 검색 - 메뉴 조회  
	public static void getMenusByShop(int shop_no) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
<<<<<<< HEAD
		int counter =1;
=======
		int counter = 1;
>>>>>>> 9589d84030599ff498cec2be2ec984b6ac4b113d
		try  {
			conn = MyConnection.getConnection();
			String sql = "SELECT * \r\n"
					+ "FROM SHOP JOIN MENU ON(shop.shop_no=menu.shop_no) \r\n"
					+ "WHERE shop.shop_no= ?";

			try{
				psmt = conn.prepareStatement(sql);
				psmt.setInt(1, shop_no);
				rs = psmt.executeQuery();
				System.out.println();
				System.out.println("해당 상점의 메뉴 목록:");
				System.out.println("순번\t메뉴이름\t메뉴설명\t판매상태");
				System.out.println("------------------------------------------------------");
				while (rs.next()) {
					int shopNo = rs.getInt("SHOP_NO");
					int menuNo = rs.getInt("MENU_NO");
					String menuName = rs.getString("MENU_NAME");
					String menuContent = rs.getString("MENU_CONTENT");
					int menuState = rs.getInt("MENU_STATE");
					Menu menu = new Menu(shop_no,menuNo,menuName,menuContent,menuState);
					menu_list.add(menu);
					if(menuState == 1) {
<<<<<<< HEAD
						System.out.println(counter +"번째 메뉴 " +menuName+ "\t"+ menuContent+ "\t"+ "핀매중");
						counter++;
						}else if(menuState==0){
							System.out.println(counter +"번째 메뉴 "+menuName +"\t"+ menuContent+"\t" + "핀매중지");
							counter++;
						}
=======
						System.out.println(counter + " : " + menuName+ "\t"+ menuContent+ "\t"+ "핀매중");
						counter++;
					}else if(menuState==0){
						System.out.println(counter + " : " + menuName +"\t"+ menuContent+"\t" + "핀매중지");
						counter++;
					}
>>>>>>> 9589d84030599ff498cec2be2ec984b6ac4b113d
				}
				System.out.println("------------------------------------------------------");
			} catch (SQLException e) {
				System.out.println("메뉴 조회 중 오류가 발생했습니다: " + e.getMessage());
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("데이터베이스 연결 중 오류가 발생했습니다: " + e.getMessage());
		} finally {
			MyConnection.close(rs, psmt, conn);
		}
	}

	public static void menuModify(int shop_no) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		//int state = 0;
		System.out.println("수정할 메뉴를 골라주세요.");
		int mch =Integer.parseInt(sc.nextLine());
		Menu menu = new Menu(menu_list.get(mch-1).getShop_no(),menu_list.get(mch-1).getMenu_no(),menu_list.get(mch-1).getMenu_name(),menu_list.get(mch-1).getMenu_content(),menu_list.get(mch-1).getMenu_state());
		System.out.println(menu.getMenu_name());
		String sql = "update menu set menu_name=?, menu_content=?, menu_state=? where menu_no='"+menu.getMenu_no()+"'";
		
		System.out.println("메뉴 이름 입력 : ");
		String newname = sc.nextLine();
		System.out.println("메뉴 소개 입력 : ");
		String newcontent = sc.nextLine();
		System.out.println("메뉴를 판매 중지 하시겠습니까? y/n");
		String yn = sc.nextLine();
		if(yn.equals("y")) {
			menu.setMenu_state(0);
		}else if(yn.equals("n")) {
			menu.setMenu_state(1);
		}else {
			System.out.println("소문자 y와 n으로 입력해주세요.");
		}
		conn=MyConnection.getConnection();
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, newname);
			psmt.setString(2, newcontent);
			psmt.setInt(3, menu.getMenu_state());
			psmt.executeUpdate();
			System.out.println("메뉴수정 완료!");
		}catch(SQLIntegrityConstraintViolationException e) {

		}catch (SQLException e) {
			System.out.println("구문입력 오류");
			e.printStackTrace();
		}
		finally{	
			menu_list.clear();
			MyConnection.close(rs, psmt, conn);
		}
	}

	public static void deleteMenu(int shop_no) throws ClassNotFoundException, SQLException {
		 Connection conn = null;
		 ResultSet rs = null;
		 PreparedStatement psmt = null;
		
		 System.out.println("삭제할 메뉴를 선택해주세요.");
		 
		 int mch =Integer.parseInt(sc.nextLine());
		 Menu menu = new Menu(menu_list.get(mch-1).getShop_no(),menu_list.get(mch-1).getMenu_no(),menu_list.get(mch-1).getMenu_name(),menu_list.get(mch-1).getMenu_content(),menu_list.get(mch-1).getMenu_state());
		 
		 String sql = "delete from menu where shop_no='"+shop_no+"'and menu_no='"+menu.getMenu_no()+"'";

		 conn = MyConnection.getConnection();
		 try {
			 psmt = conn.prepareStatement(sql);
			 psmt.executeUpdate();
			 System.out.println();
			 System.out.println("메뉴가 삭제되었습니다!");
			
		 } catch (SQLException e) {

		 }finally {
			 getAllMenus(shop_no);
			 menu_list.clear();
			 MyConnection.close(rs, psmt, conn);
		 }
		
	}

	public static void exitProgram() {
		System.out.println("프로그램을 종료합니다.");
		System.exit(0);
	}
}
