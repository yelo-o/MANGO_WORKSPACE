package com.dao.mangoplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dto.mangoplate.Shop;

public class shopController {
	Scanner sc = new Scanner(System.in);

	Shop shop;
	int shop_no;
	String shop_name;
	int shop_state;
	String shop_content;
	String shop_type;
	String ceo_id;
	List<Shop>shop_list;
	int no;
	menuController menu;
	private static int count;
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement psmt = null;

	public shopController(){
	}

	public shopController(Shop shop){
	}

	public void ceo_page(String ceo_id) {
		System.out.println("가게 이름을 적어주세요.");
		shop_name = sc.nextLine();
		System.out.println("가게를 소개해주세요.");
		shop_content = sc.nextLine();
		System.out.println("식종을 적어주세요");
		shop_type = sc.nextLine();

		Shop shop = new Shop(shop_name, shop_content, shop_type, ceo_id);
		shopController controller = new shopController();
		controller.insert(shop);
		userController.ceo_menu(ceo_id);
	}

	public int num_max() {
		String sql = "SELECT MAX(SHOP_NO) FROM SHOP";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			DBConnector.close(rs, psmt, con);
		}
		return count;

	} 
	//가게 등록 요청
	public void insert(Shop shop) {

		String sql = "insert into SHOP(Shop_no, shop_name, shop_state, shop_content, shop_type, ceo_id) values (?,?,?,?,?,?)";
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		count = num_max();
		shop.setShop_no(count);
		shop.setShop_state(0);

		try {
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, shop.getShop_no());
			psmt.setString(2, shop.getShop_name());
			psmt.setInt(3, shop.getShop_state());
			psmt.setString(4, shop.getShop_content());
			psmt.setString(5, shop.getShop_type());
			psmt.setString(6, shop.getCeo_id());
			psmt.executeUpdate();
			System.out.println("신청완료!");
		}catch(SQLIntegrityConstraintViolationException e) {

		}catch (SQLException e) {
			System.out.println("구문입력 오류");
			e.printStackTrace();
		}
		finally{	
			DBConnector.close(rs, psmt, con);
		}

	}
	//가게 정보 수정
	public void search_shop(String ceo_id) {
		String search_id = "select * from shop where ceo_id = '"+ceo_id+"'";
		shop_list = new ArrayList<Shop>();

		try {
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			psmt = con.prepareStatement(search_id);
			psmt.executeQuery();
			rs = psmt.executeQuery();

			while(true) {
				rs.next();
				shop_no=rs.getInt(1);
				shop_name = rs.getString(2);
				shop_state = rs.getInt(3);
				shop_content = rs.getString(4);
				shop_type = rs.getString(5);
				Shop shop = new Shop(shop_no, shop_name,shop_state,shop_content,shop_type,ceo_id); 
				shop_list.add(shop);

				if(shop_state==0) {
					System.out.println("-------------------------------승인 대기중인 가게입니다.--------------------------------");
					System.out.println("가게 고유 번호 : "+shop_no+"\n가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
					System.out.println("승인 대기중");
				}else if(shop_state==2) {
					System.out.println("-------------------------------철회 요청중인 가게입니다.--------------------------------");
					System.out.println("가게 고유 번호 : "+shop_no+"\n가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
					System.out.println("입점 철회 요청중");
				}else if(shop_state==1) {
					System.out.println("-------------------------------정상 영업중인 가게입니다.--------------------------------");
					System.out.println("가게 고유 번호 : "+shop_no+"\n가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
					System.out.println("정상 영업중");
				}
				if(rs==null) {
					break;
				}
			}
		}catch(SQLException e) {
		}finally {
			DBConnector.close(rs, psmt, con);
		}
		//userController.ceo_menu(ceo_id);
	}



	public void revokeShop_Request(String user_id,int user_type) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException e) {

		} catch (SQLException e) {

		}
		System.out.println("영업을 철회할 가게의 고유번호를 입력하세요.");
		shop_no = Integer.parseInt(sc.nextLine());
		String sql = "insert into wait_shop select * from shop where shop_no='"+shop_no+"'";
		String sql2 = "update shop set shop_state='2' where shop_no='"+shop_no+"'";
		try {
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
			psmt.executeUpdate(sql2);
			System.out.println("철회신청이 완료되었습니다.");
		} catch (SQLException e) {
		}finally {
			DBConnector.close(rs, psmt, con);
		}
		userController.ceo_menu(user_id);
	}



	public void revokeCancel_Request(String user_id,int user_type) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("영업 철회를 취소할 가게의 고유번호를 입력하세요.");
		shop_no = sc.nextInt();
		String sql2 = "insert into shop select * from wait_shop where shop_no='"+shop_no+"'";
		String sql = "delete from shop where shop_no='"+shop_no+"'";
		String sql3 = "delete from wait_shop where shop_no='"+shop_no+"'";
		try {
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
			psmt.executeUpdate(sql2);
			psmt.executeUpdate(sql3);
			System.out.println("철회 취소 신청이 완료되었습니다.");
		} catch (SQLException e) {
		}finally {
			DBConnector.close(rs, psmt, con);
		}
		userController.ceo_menu(user_id);
	}


	public void modify_shopInfo(String ceo_id) {
		menuController menu= new menuController();

		String search_name = "select * from shop where ceo_id='"+ceo_id+"'";
		int counter=1;
		try {
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException e) {


		} catch (SQLException e) {

		}
		shop_list = new ArrayList<Shop>();
		try {
			psmt = con.prepareStatement(search_name);
			rs= psmt.executeQuery();
			while(true){
				if(rs==null) {
				}
				rs.next();
				shop_no = rs.getInt(1);
				shop_name = rs.getString(2);
				shop_state = rs.getInt(3);
				shop_content = rs.getString(4);
				shop_type = rs.getString(5);
				ceo_id = rs.getString(6);
				shop = new Shop(shop_no,shop_name,shop_state,shop_content,shop_type,ceo_id);
				shop_list.add(shop);
				System.out.println(counter+" : "+shop_name);
				counter++;

			}
		} catch (SQLException e) {
		}
		System.out.println("수정할 가게 선택");
		int shop_ch = Integer.parseInt(sc.nextLine());
		System.out.println("1 : 음식점 정보 수정, 2 : 메뉴 수정");
		String modi_ch = sc.nextLine();

		if(modi_ch.equals("1")) {
			System.out.println("수정 내용 입력");
			System.out.println("가게 이름 : ");
			String shopname = sc.nextLine();

			System.out.println("가게 소개 : ");
			String shopcontent = sc.nextLine();

			System.out.println("가게 식종 : ");
			String shoptype =sc.nextLine();

			Shop shop = new Shop(shop_list.get(shop_ch-1).getShop_name(),shop_list.get(shop_ch-1).getShop_content(),shop_list.get(shop_ch-1).getShop_type());


			String modishop_sql = "update SHOP set shop_name=?, shop_content=?, shop_type=? "
					+ "where ceo_id='" + ceo_id +"'and shop_name='"+shop.getShop_name()+"'and shop_content='"+shop.getShop_content()+"'and shop_type='"+shop.getShop_type()+"'";
			try {
				psmt = con.prepareStatement(modishop_sql);
				psmt.setString(1, shopname);
				psmt.setString(2, shopcontent);
				psmt.setString(3, shoptype);
				psmt.executeUpdate();

				System.out.println("변경 완료!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBConnector.close(rs, psmt, con);
			}
		}else if(modi_ch.equals("2")) {


			shop =new Shop(shop_list.get(shop_ch-1).getShop_no(),shop_list.get(shop_ch-1).getShop_name(),shop_list.get(shop_ch-1).getShop_state(),shop_list.get(shop_ch-1).getShop_content(),shop_list.get(shop_ch-1).getShop_type(),shop_list.get(shop_ch-1).getCeo_id());
			int no = shop_list.get(shop_ch-1).getShop_no();
			menu.MenuSearch(ceo_id,no);

			System.out.println("1 : 메뉴 추가, 2 : 메뉴 수정, 3 : 메뉴 삭제");
			String menu_ch = sc.nextLine();
			//메뉴추가
			if(menu_ch.equals("1")) {
				String getShopno = "select shop_no from shop where ceo_id='" + ceo_id +"'and shop_name='"+shop.getShop_name()+"'and shop_content='"+shop.getShop_content()+"'and shop_type='"+shop.getShop_type()+"'";

				try {
					psmt.close();
					psmt = con.prepareStatement(getShopno);
					rs = psmt.executeQuery();
					rs.next();
					int shopno=rs.getInt(1);

					menu.menuInsert(ceo_id, shopno);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					DBConnector.close(rs, psmt, con);
				}

			}else if(menu_ch.equals("2")){
				//메뉴 수정 메소드
				menu.menuModify(ceo_id);

			}else if(menu_ch.equals("3")) {
				//메뉴 삭제
				menu.deleteMenu(ceo_id,shop.getShop_no());
			}
		}
		userController.ceo_menu(ceo_id);
	}

}



