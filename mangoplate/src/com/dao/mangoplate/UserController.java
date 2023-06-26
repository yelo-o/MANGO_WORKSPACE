package com.dao.mangoplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;
import com.dao.mangoplate.MyConnection;
import com.dto.mangoplate.User;
import com.repository.mangoplate.AdminRepository;
import com.repository.mangoplate.CeoRepository;
import com.repository.mangoplate.CusRepository;
import com.repository.mangoplate.ReviewRepository;
import com.repository.mangoplate.UserRepository;

public class UserController {
	private User user;
	static Scanner sc = new Scanner(System.in);

	static String userID;
	static String passwd;
	static String name;
	static String address;
	static String phone;
	static String email;
	static String birth;
	static AdminRepository admin =new AdminRepository();
	//
	static String verifiedID;
	//
	private static int user_type;
	static Connection conn = null;

	//로그인
	

	public static void shop_menu(int selectShop) throws ClassNotFoundException, SQLException {
		CusRepository CusRep = new CusRepository();
		System.out.println("1 : 메뉴보기, 2 : 후기보기, 3 : 후기작성, 4 : 처음으로 돌아가기");
		String cus_ch = sc.nextLine();

		if (cus_ch.equals("1")) {
			//메뉴보기
			MenuController.getMenusByShop(selectShop);
			shop_menu(selectShop);
		} else if (cus_ch.equals("2")) {
			//후기보기
			ReviewRepository.readReaview(selectShop);
			shop_menu(selectShop);
		} else if (cus_ch.equals("3")) {
			//후기작성
			ReviewRepository.insertReview(selectShop);
			shop_menu(selectShop);
			
		}else if(cus_ch.equals("4")) {
			UserController.cus_menu();
		}
	}
	//일반고객 페이지
	public static void cus_menu() throws ClassNotFoundException, SQLException {
		System.out.println("망고플레이트에 오신걸 환영합니다!");
		System.out.println("원하시는 기능을 선택해주세요!");
		System.out.println("1 : 음식점 조회, 2 : 음식점 검색, 3 : 내 리뷰 보기, 4 : 회원정보 수정");
		String cus_ch = sc.nextLine();

		if (cus_ch.equals("1")) {
			//1. 음식점 조회 메소드 호출
			System.out.println("1 : 전체 음식점 조회, 2 : 카테고리별 음식점 조회, 3 : 평점순 음식점 조회");
			String ch = sc.nextLine();
			
			if(ch.equals("1")) {
				//전체음식점 조회
				CusRepository.cusAllShopList();
				int select = CusRepository.cusSelectShop();
				shop_menu(select);
			}else if(ch.equals("2")) {
				//카테고리별 음식점 조회
				CusRepository.cusCategoryShopList();
				int select = CusRepository.cusSelectShop();
				shop_menu(select);
			}else if(ch.equals("3")) {
				//평점순 음식점 조회
				CusRepository.cusRateHighShopList();
				int select = CusRepository.cusSelectShop();
				shop_menu(select);
			}
		} else if (cus_ch.equals("2")) {
			//2. 음식점 검색 메소드 호출
			CusRepository.cusSearchShop();
			int select = CusRepository.cusSelectShop();
			shop_menu(select);
		} else if (cus_ch.equals("3")) {
			//3. 내 리뷰 보기 메소드 호출
			ReviewRepository.readMyReview();
			UserController.cus_menu();

		} else if (cus_ch.equals("4")) {
			//4. 회원정보 수정 메소드 호출
			UserRepository.user_modify();
			UserController.cus_menu();
		}
	}
	public static void ceo_menu() throws ClassNotFoundException, SQLException {
		CeoRepository ceo = new CeoRepository();
		
		System.out.println("점주님 음식점 관리 메뉴를 보여드릴게요!");
		System.out.println("1 : 음식점 승인 요청, 2 : 음식점 수정, 3 : 내 가게 조회, 4 : 음식점 철회 요청, 5 : 철회 요청 취소, 9 : 종료");
		String ceo_ch = sc.nextLine();
		
		if (ceo_ch.equals("1")) {
			//1. 음식점 승인 요청 메소드 호출
			ceo.insert_info();
			ceo_menu();
		} else if (ceo_ch.equals("2")) {
			//2. 음식점 수정 메소드 호출
			ceo.modify_shopInfo();
			ceo_menu();
		} else if (ceo_ch.equals("3")) {
			//3. 내 가게 조회
			ceo.search_shop();
			ceo_menu();
		} else if (ceo_ch.equals("4")) {
			//4. 음식점 철회 요청 메소드 호출
			ceo.revokeShop_Request();
			ceo_menu();
		} else if (ceo_ch.equals("5")) {
			//5. 철회 요청 취소 메소드 호출
			ceo.revokeCancel_Request();
			ceo_menu();
		}

	}
	
	public static void admin_menu() throws ClassNotFoundException, SQLException {
		if(user_type != 0) {
			System.out.println("나가!");
			UserRepository.login();
		}
		System.out.println("원하시는 기능을 선택해주세요");
		System.out.println("1 : 음식점 승인요청, 2 : 음식점 철회요청, 3 : 활동 중인 음식점 조회, 4 : 음식점 강제 철회 9 : 종료");
		String admin_ch = sc.nextLine();
		
		if(admin_ch.equals("1")) {
			//승인요청조희 메소드
			admin.acceptRequest();
		}
		else if(admin_ch.equals("2")) {
			//철회요청조회
			admin.revokeRequest();
		}
		else if(admin_ch.equals("3")) {
			//활동 중인 음식점 조회
			admin.searchActiveShop();
		}
		else if(admin_ch.equals("4")) {
			//철회 메서드
			admin.kickShop();
		}
	}


}


