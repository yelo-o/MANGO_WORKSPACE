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

//System.out.println("전체 메뉴 조회");
//System.out.println("1 : 메뉴 등록, 2 : 메뉴 수정, 3 : 메뉴 삭제");
//System.out.println("번호를 선택하세요: ");
//menu_ch = sc.nextLine();

import com.dto.mangoplate.Menu;
import com.dto.mangoplate.Shop;
public class menuController {
	Scanner sc = new Scanner(System.in);
//	static String url = "jdbc:oracle:thin:@localhost:1521/xe";
//	static String id = "loacalmg";
//	static String pw = "localmg";
	List<Menu>menu_list;
	
	private static int count;
	int shop_no;
	int menu_no;
	String menu_name;
	String menu_content;
	
	int menu_state;
	
	Menu menu = new Menu();
	String menuType;
	
	public static int num_max() {
		String sql = "SELECT MAX(MENU_NO) FROM MENU";
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			rs.next();
			count = rs.getInt(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		return count;

	} 
	public void MenuSearch(String ceo_id,int shop_no) {
		int counter=1;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String search_menu = "select * from menu where shop_no = '"+shop_no+"'";
			
			menu_list = new ArrayList<Menu>();
			
			try {
				psmt = con.prepareStatement(search_menu);
				rs= psmt.executeQuery();
				System.out.println("--------------------------------------------------");
				while(true){
					if(rs==null) {
						System.out.println("등록된 메뉴정보가 없습니다.");
					}
					rs.next();
					menu_no = rs.getInt(2);
					menu_name = rs.getString(3);
					menu_content = rs.getString(4);
					menu_state = rs.getInt(5);
					menu = new Menu(shop_no,menu_no,menu_name,menu_content,menu_state);
					menu_list.add(menu);
					if(menu_state == 1) {
					System.out.println(counter+"번 메뉴 : "+menu_name+"      정상판매중");
					counter++;}
					else if(menu_state == 0) {
						System.out.println(counter+"번 메뉴 : "+menu_name+"      판매중지");
						counter++;
					}

				}
			} catch (SQLException e) {
			}finally {
				System.out.println("--------------------------------------------------");
				MyConnection.close(rs, psmt, con);
			}
	}
	//가게 등록 요청
	public void menuInsert(String ceo_id,int shop_no) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		String sql = "insert into MENU(Shop_no, menu_no,menu_name,menu_content,menu_state) values (?,?,?,?,?)";
		
		
		count = num_max();
		count++;
		menu.setShop_no(shop_no);
		menu.setMenu_no(count);
		menu.setMenu_state(1);
		System.out.println(shop_no);
		System.out.println("메뉴를 추가해주세요.");
		System.out.println("메뉴 이름 : ");
		menu_name = sc.nextLine();
		System.out.println("메뉴 설명 : ");
		menu_content = sc.nextLine();
		
		menu = new Menu(menu.getShop_no(), menu.getMenu_no(),menu_name, menu_content, menu.getMenu_state());
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		try {
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, menu.getShop_no());
			psmt.setInt(2, menu.getMenu_no());
			psmt.setString(3, menu.getMenu_name());
			psmt.setString(4, menu.getMenu_content());
			psmt.setInt(5, menu.getMenu_state());
			psmt.executeUpdate();
			System.out.println("메뉴추가 완료!");
		}catch(SQLIntegrityConstraintViolationException e) {

		}catch (SQLException e) {
			System.out.println("구문입력 오류");
			e.printStackTrace();
		}
		finally{	
			MyConnection.close(rs, psmt, con);
		}
	}
	
	public void menuModify(String ceo_id) {
		Connection con = null;
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
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		try {
			psmt = con.prepareStatement(sql);
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
			MyConnection.close(rs, psmt, con);
		}
	}
	public void deleteMenu(String ceo_id, int shop_no) {
		 Connection con = null;
		 ResultSet rs = null;
		 PreparedStatement psmt = null;
		
		 System.out.println("삭제할 메뉴를 선택해주세요.");
		 
		 int mch =Integer.parseInt(sc.nextLine());
		 Menu menu = new Menu(menu_list.get(mch-1).getShop_no(),menu_list.get(mch-1).getMenu_no(),menu_list.get(mch-1).getMenu_name(),menu_list.get(mch-1).getMenu_content(),menu_list.get(mch-1).getMenu_state());
		 
		 String sql = "delete from menu where shop_no='"+shop_no+"'and menu_no='"+menu.getMenu_no()+"'";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB에 접근할수 없습니다.");
		}
		
		try {
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
			System.out.println();
			System.out.println("메뉴가 삭제되었습니다!");
			
		} catch (SQLException e) {

		}finally {
			MenuSearch(ceo_id,shop_no);
			menu_list.clear();
			MyConnection.close(rs, psmt, con);
		}
		
	}
}
