package com.dao.mangoplate;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dto.mangoplate.Shop;

public class ReviewController {
	Scanner sc = new Scanner(System.in);
	int count = 0;

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	Shop shop = new Shop();
	int shop_no = 1;
	String ceo_id = "mango"; //로그인할 때 받아오는 user_id

	//메인 메뉴
	public void reviewPage() {
		System.out.println("메뉴: 1.리뷰 작성 | 2.리뷰 읽기 | 3.내 리뷰 보기 | 9.종료");
		int selection =Integer.parseInt(sc.nextLine());
		switch(selection) {
		case 1:
			insertReview(shop_no, ceo_id);
			reviewPage(); //리뷰 페이지로 다시 돌아오기
		case 2:
			readReaview(ceo_id);
			reviewPage();
		case 3:
			readMyReview(ceo_id);
			reviewPage();
		case 9:
			exit();
			break;
		}
	}

	public int num_max() {
		String sql = "SELECT MAX(review_no) FROM shop_review";
		try {
			conn = MyConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			count = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return count;
	} 

	//1. 리뷰 작성
	public void insertReview(int shop_no, String ceo_id) {
		//(1) DB연결 준비
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		//(2) 입력받은 변수값을 토대로 SQL문 구성하여 실행해서 DB데이터 업데이트
		try {
			count = num_max(); //현재 DB에서 행이 몇개 인지 받아오기
			count++;
			
			String sql = "INSERT INTO shop_review(shop_no, review_no, writer, review_content,\r\n"
					+ "review_date, review_rating)\r\n"
					+ "VALUES(?, ?, ?, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);

			System.out.println("내용을 입력하세요");
			String reviewContent = sc.nextLine();
			System.out.println("별점을 입력하세요");
			int reviewRating = Integer.parseInt(sc.nextLine());

			pstmt.setInt(1, shop_no);
			pstmt.setInt(2, count);
			pstmt.setString(3, ceo_id);
			pstmt.setString(4, reviewContent);
			pstmt.setInt(5, reviewRating);
			pstmt.executeUpdate();
			System.out.println("리뷰 추가 완료!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}

	//2. 모든 리뷰 읽기
	public void readReaview(String ceo_id) {
		//(1) DB연결 준비
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//(2) SQL문 실행 후 결과 불러오기
		try {
			String sql = "SELECT * FROM shop_review ORDER BY review_no DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.printf("%-10s%-15s%-12s%-20s%-40s\n", "리뷰번호", "작성자", "별점", "작성일자", "리뷰 내용");
			count = 1;
			while(rs.next()) {
				int reviewNo = rs.getInt("review_no");
				String reviewWriter = rs.getString("writer");
				String reviewContent = rs.getString("review_content");
				Date reviewDate = rs.getDate("review_date");
				int reviewRating = rs.getInt("review_rating");
				System.out.printf("%-10s%-15s%-12s%-20s%-40s\n", reviewNo, reviewWriter, reviewRating, reviewDate, reviewContent);
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}

	//3. 내 리뷰 보기
	public void readMyReview(String ceo_id) {
		//(1) DB연결 준비
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//(2) SQL문 실행 후 결과 불러오기
		try {
			String sql = "SELECT * FROM shop_review WHERE writer = '"+ceo_id+"'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.printf("%-10s%-15s%-12s%-20s%-40s\n", "리뷰번호", "작성자", "별점", "작성일자", "리뷰 내용");
			
			while(rs.next()) {
				int reviewNo = rs.getInt("review_no");
				String reviewWriter = rs.getString("writer");
				String reviewContent = rs.getString("review_content");
				Date reviewDate = rs.getDate("review_date");
				int reviewRating = rs.getInt("review_rating");
				System.out.printf("%-10s%-15s%-12s%-20s%-40s\n", reviewNo, reviewWriter, reviewRating, reviewDate, reviewContent);
			}

			System.out.println("******************************************************************************");
			System.out.println("*****************************************보조메뉴: 1.리뷰 수정 | 2.리뷰 삭제 | 9.종료**");
			System.out.println("******************************************************************************");
			System.out.println();
			int selection = Integer.parseInt(sc.nextLine());
			if(selection==1) { //리뷰 수정
				modifyReview();
			} else if(selection==2) { //리뷰 삭제
				deleteReview();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			MyConnection.close(rs, pstmt, conn);
		}

	}

	//3-1. 리뷰 수정
	public void modifyReview() {
		try {
			System.out.println("수정을 원하는 리뷰번호를 입력하세요");
			int reviewNo = Integer.parseInt(sc.nextLine());

			System.out.println("수정 내용을 입력하세요");
			String reviewContent = sc.nextLine();

			String updateSQL = "UPDATE shop_review SET review_content=? WHERE review_no=?";

			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, reviewContent);
			pstmt.setInt(2, reviewNo);
			pstmt.executeUpdate();
			System.out.println("리뷰 수정 완료!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}

	//3-2. 리뷰 삭제
	public void deleteReview() {
		try {
			System.out.println("삭제를 원하는 리뷰번호를 입력하세요");
			int reviewNo = Integer.parseInt(sc.nextLine());

			String deleteSQL = "DELETE shop_review WHERE review_no = ?";
			pstmt = conn.prepareStatement(deleteSQL);

			pstmt.setInt(1, reviewNo);
			pstmt.executeUpdate();
			System.out.println("리뷰 삭제 완료!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}

	// 종료
	public void exit() {
		MyConnection.close(rs, pstmt, conn);
//		if(conn != null) {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//			}
//		}
		System.out.println("** 종료 **");
		System.exit(0);
	}

}
