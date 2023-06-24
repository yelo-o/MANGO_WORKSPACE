package com.dao.mangoplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dto.mangoplate.Shop;

public class CusRepository 
{
	Shop shop;
	int shop_no;
	String shop_name;
	int shop_state;
	String shop_content;
	String shop_type;
	String ceo_id;
	List<Shop>shop_list;
	int no;
	MenuController menu;
	shopController shopcontroll;
	private static int count;
	Scanner sc = new Scanner(System.in); 
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement psmt = null;
	
	public void cusAllShopList() {
		/**
		 * @author mingyu
		 */
		System.out.println("verifiedID : " + userController.verifiedID);
		
		String search_shop = "select * from shop where shop_state = '1'";
		int counter = 1;
		shop_list = new ArrayList<Shop>();
		//DB연결 준비
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			psmt = con.prepareStatement(search_shop);
			rs = psmt.executeQuery();
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
	
	public void cusCategoryShopList() {
		String shoptype="???";
		int counter = 1;
		shop_list = new ArrayList<Shop>();
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
        //DB연결 준비
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			psmt = con.prepareStatement(categoryList);
			psmt.executeQuery();
			rs = psmt.executeQuery();
			System.out.println("--------------------------------------------------");
			while(rs.next()) {
//				rs.next();
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
			userController.cus_menu();
		}
	}
	public void cusRateHighShopList() {
		String search_shop = "select * from shop where shop_state = '1' and where ";
		int counter = 1;
		shop_list = new ArrayList<Shop>();
        //DB연결 준비
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			psmt = con.prepareStatement(search_shop);
			psmt.executeQuery();
			rs = psmt.executeQuery();
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
	
}
