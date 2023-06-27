package com.repository.mangoplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dto.mangoplate.Shop;
import com.dto.mangoplate.User;
import com.dao.mangoplate.MyConnection;
import com.dao.mangoplate.UserController;

public class AdminRepository {
	UserController user = new UserController();
	static Scanner sc = new Scanner(System.in);
	List<Shop>shop_list;

	List<List<String>> tableData =new ArrayList<>();
	String shop_no;
	String shop_name;
	int shop_state;
	String shop_content;
	String shop_type;
	String ceo_id;

	public void searchRequest () throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		//상태가 0일경우 승인요청 대기중
		String sql = "select * from shop where shop_state='0'"; 

		con= MyConnection.getConnection();
		
		System.out.println("승인을 요청한 음식점 목록 입니다.");
		List<String> header = new ArrayList<>();
		header.add("사업자번호");
		header.add("음식점이름");
		header.add("음식점상태");
		header.add("음식점타입");
		header.add("점주아이디");
		tableData.add(header);
		List<Shop>shop_list = new ArrayList<>();

		try {
			psmt = con.prepareStatement(sql);
			rs= psmt.executeQuery();
			while(true){
				if(rs==null) {
				}
				rs.next();
				int shop_no = rs.getInt(1);
				String shop_name = rs.getString(2);
				int shop_state = rs.getInt(3);
				String shop_type = rs.getString(5);
				String ceo_id = rs.getString(6);

				Shop shop = new Shop(shop_no,shop_name,shop_state,shop_type,ceo_id);
				shop_list.add(shop);
				List<String> row = new ArrayList<>();
				row.add(Integer.toString(shop_no));
				row.add(shop_name);
				row.add(Integer.toString(shop_state));
				row.add(shop_type);
				row.add(ceo_id);
				tableData.add(row);
			}
		}catch (SQLException e) {
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		for (List<String> rowprint : tableData) {
			for (String cell : rowprint) {
				System.out.print(cell +"\t" +"\t"+"\t");
				// 탭으로 셀 구분
			}
			System.out.println(); // 행 구분을 위한 개행
		}
		tableData.clear();

	}


	public void acceptRequest() throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		searchRequest();
		System.out.println(User.verifiedAdminID+"님 승인할 가게의 사업자 번호를 입력하세요.");
		String shop_no =  sc.nextLine();

		con= MyConnection.getConnection();
		
		String sql = "update shop set shop_state='1' where shop_no='"+shop_no+"' and shop_state='0'";
		try {
			psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		System.out.println("승인 되었습니다!");
		UserController.admin_menu();
	}

	public void revokeRequest() throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		
		revokeSearchRequest();
		
		System.out.println("철회요청을 승인할 사업자번호를 입력해주세요.");
		String shop_no = sc.nextLine();
		
		String sql = "delete from wait_shop where shop_no='"+shop_no+"'";
		String sql2 = "delete from shop where shop_no='"+shop_no+"'";
		
		con= MyConnection.getConnection();
		try {
			psmt= con.prepareStatement(sql);
			psmt.executeUpdate();
			psmt.executeUpdate(sql2);
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		UserController.admin_menu(); 
	}


	public void revokeSearchRequest() throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		String sql = "select * from wait_shop"; 

		con= MyConnection.getConnection(); 
		
		System.out.println("철회를 요청한 음식점 목록 입니다.");
		List<String> header = new ArrayList<>();
		header.add("사업자번호");
		header.add("음식점이름");
		header.add("음식점상태");
		header.add("음식점타입");
		header.add("점주아이디");
		tableData.add(header);
		List<Shop>shop_list = new ArrayList<>();

		try {
			psmt = con.prepareStatement(sql);
			rs= psmt.executeQuery();
			while(true){
				if(rs==null) {
				}
				rs.next();
				int shop_no = rs.getInt(1);
				String shop_name = rs.getString(2);
				int shop_state = rs.getInt(3);
				String shop_type = rs.getString(5);
				String ceo_id = rs.getString(6);

				Shop shop = new Shop(shop_no,shop_name,shop_state,shop_type,ceo_id);
				shop_list.add(shop);
				List<String> row = new ArrayList<>();
				row.add(Integer.toString(shop_no));
				row.add(shop_name);
				row.add(Integer.toString(shop_state));
				row.add(shop_type);
				row.add(ceo_id);
				tableData.add(row);
			}
		}catch (SQLException e) {
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		for (List<String> rowprint : tableData) {
			for (String cell : rowprint) {
				System.out.print(cell + "\t"+"\t"); // 탭으로 셀 구분
			}
			System.out.println(); // 행 구분을 위한 개행
		}tableData.clear();

	}

	public void searchActiveShop() throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		String search_shop = "select * from shop where shop_state = '1'";

		con= MyConnection.getConnection();
		try {
			psmt = con.prepareStatement(search_shop);
			psmt.executeQuery();
			rs = psmt.executeQuery();

			while(rs.next()) {
				shop_no=rs.getString(1);
				shop_name = rs.getString(2);
				shop_state = rs.getInt(3);
				shop_type = rs.getString(5);
				ceo_id = rs.getString(6);
				
				if(shop_state==1) {
					System.out.println("-------------------------------정상 영업중인 가게입니다.--------------------------------");
					System.out.println("가게 고유 번호 : "+shop_no+"\n가게 이름 : " +shop_name+"\n식종 : "+shop_type+"\n등록자 아이디 : "+ceo_id);
					System.out.println("정상 영업중");
				}
			}
		}catch(SQLException e) {
		}finally {
			MyConnection.close(rs, psmt, con);
		}
		UserController.admin_menu();
	}


	public void kickShop() throws ClassNotFoundException, SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		String sql = "select * from shop";


		con= MyConnection.getConnection();

		try {
			psmt = con.prepareStatement(sql);
			psmt.executeQuery();
			rs = psmt.executeQuery();

			while(rs.next()) {
				shop_no=rs.getString(1);
				shop_name = rs.getString(2);
				shop_state = rs.getInt(3);
				shop_type = rs.getString(5);
				ceo_id = rs.getString(6);

				System.out.println("-----------------------------------------------------------------------------");
				System.out.println("가게 고유 번호 : "+shop_no+"\n가게 이름 : " +shop_name+"\n식종 : "+shop_type+"\n등록자 아이디 : "+ceo_id);
			}
		}catch(SQLException e) {
		}
		System.out.println("강제 철회할 가게의 고유번호를 입력하세요.");
		String kick_no = sc.nextLine();
		String name;
		String search = "select shop_name from shop where shop_no='"+kick_no+"'";
		String kick_sql = "delete from shop where shop_no='"+kick_no+"'";
		try {
			psmt = con.prepareStatement(search);
			rs = psmt.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
				psmt.executeUpdate(kick_sql);
				System.out.println("고유번호 " + kick_no + "의 " + name + "가게가 강제 철회되었습니다.");
			} else {
				System.out.println("해당 고유번호의 가게를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {

		}finally {
			MyConnection.close(rs, psmt, con);
		}
		UserController.admin_menu();
	}
}
