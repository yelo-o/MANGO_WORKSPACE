package com.repository.mangoplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dao.mangoplate.MenuController;
import com.dao.mangoplate.MyConnection;
import com.dto.mangoplate.Shop;
class Model{
	int shopno;
	String shopname;
	float modelrating;
	
	public Model(int shopno, String shopname, float modelrating) {
		super();
		this.shopno = shopno;
		this.shopname = shopname;
		this.modelrating = modelrating;
	}
}

public class CusRepository 
{
	Shop shop;
	static int shop_no;
	static String shop_name;
	static String shop_content;
	static String shop_type;
	static String ceo_id;
	List<Model>rate_list;
	static List<Shop>shop_list;
	float rating;
	MenuController menu;
	static Scanner sc = new Scanner(System.in); 
	
	
	public static void cusAllShopList() throws ClassNotFoundException, SQLException {
		String search_shop = "select * from shop where shop_state = '1'";
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		int counter = 1;
		shop_list = new ArrayList<Shop>();
		
		con= MyConnection.getConnection();
		try {
			psmt = con.prepareStatement(search_shop);
			psmt.executeQuery();
			rs = psmt.executeQuery();
			System.out.println();
			System.out.println("------------------------------------------------------------------");
			while(true) {
				rs.next();
				shop_no=rs.getInt(1);
				shop_name = rs.getString(2);
				shop_content = rs.getString(4);
				shop_type = rs.getString(5);
				Shop shop = new Shop(shop_no, shop_name,shop_content,shop_type,ceo_id); 
				shop_list.add(shop);
				System.out.println(counter+") 가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
				counter++;
				System.out.println("------------------------------------------------------------------");
				
			}
		}catch(SQLException e) {
		}finally {
			MyConnection.close(rs, psmt, con);
		}
	}
	
	public static int cusSelectShop() {
		System.out.println("조회하실 가게를 선택하세요.");
		int shop_ch = Integer.parseInt(sc.nextLine());
		int shop_no = shop_list.get(shop_ch-1).getShop_no(); 
		shop_list.clear();
		return shop_no;
	}
	public static void cusCategoryShopList() throws ClassNotFoundException, SQLException {
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		String shoptype="???";
		int counter = 1;
		shop_list = new ArrayList<Shop>();
		System.out.println();
		System.out.println("------------------------------------------------------------------");
		System.out.println("카테고리");
		System.out.println("1 : 한식, 2 : 양식, 3 : 중식, 4 : 일식, 5 : 패스트 푸드, 6 : 분식, 7 : 카페");
		String category = sc.nextLine();
		if(category.equals("1")) {
			shoptype="한식";
		}else if(category.equals("2")) {
			shoptype="양식";
		}else if(category.equals("3")) {
			shoptype="중식";
		}else if(category.equals("4")) {
			shoptype="일식";
		}else if(category.equals("5")) {
			shoptype="패스트푸드";
		}else if(category.equals("6")) {
			shoptype="분식";
		}else if(category.equals("7")) {
			shoptype="카페";
		}else {
			System.out.println("카테고리 번호를 입력해주세요.");
		}
		String categoryList = "select * from shop where shop_state = '1' and shop_type='"+shoptype+"'";
		con= MyConnection.getConnection();
		try {
			psmt = con.prepareStatement(categoryList);
			psmt.executeQuery();
			rs = psmt.executeQuery();
			System.out.println();
			System.out.println("--------------------------------------------------");
			while(true) {
				rs.next();
				shop_no=rs.getInt(1);
				shop_name = rs.getString(2);
				shop_content = rs.getString(4);
				shop_type = rs.getString(5);
				Shop shop = new Shop(shop_no, shop_name,shop_content,shop_type,ceo_id); 
				shop_list.add(shop);
				System.out.println(counter+") 가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
				counter++;
				System.out.println("--------------------------------------------------");
				
			}
		}catch(SQLException e) {
		}finally {
			MyConnection.close(rs, psmt, con);
		}
	}
	
	public static void cusRateHighShopList() throws ClassNotFoundException, SQLException {
		String search_shop = "SELECT s.shop_no, s.shop_name, s.shop_content, s.shop_type, AVG(sr.rating) AS avg_rating FROM shop s JOIN shop_review sr ON s.shop_no = sr.shop_no GROUP BY s.shop_no, s.shop_name, s.shop_content, s.shop_type ORDER BY avg_rating DESC, s.shop_name DESC";
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		int counter = 1;
		shop_list = new ArrayList<Shop>();
		
		con= MyConnection.getConnection();
		try {
			psmt = con.prepareStatement(search_shop);
			psmt.executeQuery();
			rs = psmt.executeQuery();
			System.out.println();
			System.out.println("                    높은 평점을가진 순서로 보여드릴게요!                    ");
			System.out.println("------------------------------------------------------------------");
			while(rs.next()) {
				
				shop_no=rs.getInt(1);
				shop_name = rs.getString(2);
				shop_content = rs.getString(3);
				shop_type = rs.getString(4);
				float avg = rs.getFloat(5);
				Shop shop = new Shop(shop_no, shop_name,shop_content,shop_type,ceo_id); 
				shop_list.add(shop);
				System.out.println(counter+") 가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type+"\n평점 : "+avg);
				counter++;
				System.out.println("------------------------------------------------------------------");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, psmt, con);
		}
	}
	public static void cusSearchShop() throws ClassNotFoundException, SQLException {
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		int counter = 1;
		shop_list = new ArrayList<Shop>();
		
		System.out.println("검색어를 입력해주세요 : ");
		String str= sc.nextLine();
		
		String search_shop = "select * from shop where shop_state = '1' and shop_name like '%"+str+"%'";
		
		con= MyConnection.getConnection();
		
		try {
			psmt = con.prepareStatement(search_shop);
			psmt.executeQuery();
			rs = psmt.executeQuery();
			System.out.println();
			System.out.println("------------------------------------------------------------------");
			while(rs.next()) {
				shop_no=rs.getInt(1);
				shop_name = rs.getString(2);
				shop_content = rs.getString(4);
				shop_type = rs.getString(5);
				Shop shop = new Shop(shop_no, shop_name,shop_content,shop_type,ceo_id); 
				shop_list.add(shop);
				System.out.println(counter+") 가게 이름 : " +shop_name+"\n가게 소개 : "+shop_content+"\n식종 : "+shop_type);
				counter++;
				System.out.println("------------------------------------------------------------------");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, psmt, con);
		}
	}
}
