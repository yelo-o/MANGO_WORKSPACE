package com.dao.mangoplate;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dto.mangoplate.Shop;
import com.dto.mangoplate.User;
import com.repository.mangoplate.ReviewRepository;

public class ReviewController {
	

	Shop shop = new Shop();
	String ceo_id = User.verifiedCeoID; //로그인할 때 받아오는 user_id

	//메인 메뉴
	public static void reviewPage(int shop_no) throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("메뉴: 1.리뷰 작성 | 2.리뷰 읽기 | 3.내 리뷰 보기 | 9.종료");
		int selection =Integer.parseInt(sc.nextLine());
		switch(selection) {
		case 1:
			ReviewRepository.insertReview(shop_no);
			reviewPage(shop_no); //리뷰 페이지로 다시 돌아오기
		case 2:
			ReviewRepository.readReaview(shop_no);
			reviewPage(shop_no);
		case 3:
			ReviewRepository.readMyReview();
			reviewPage(shop_no);
		case 9:
			break;
		}
	}

	
	// 종료


}
