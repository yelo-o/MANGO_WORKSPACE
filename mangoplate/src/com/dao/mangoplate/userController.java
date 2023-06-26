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
	public static void login() {
		String userID;
		String passwd;

		System.out.println("ID를 입력하세요");
		userID = sc.nextLine();
		System.out.println("비밀번호를 입력하세요");
		passwd = sc.nextLine();

		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String loginSQL =
				"SELECT name, user_type, userid, passwd\r\n"
						+ "FROM mango_user\r\n"
						+ "WHERE userid = ? AND passwd = ?";
		try {
			pstmt = conn.prepareStatement(loginSQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				throw new SQLException();
			} else {
				String user_name = rs.getString(1);
				int user_type = rs.getInt(2);
				String user_ID = rs.getString(3);
				System.out.println("안녕하세요 " +user_name+"님 반갑습니다.");

				if (user_type == 1) {
					User.verifiedCeoID = user_ID; //로그인된 점주 아이디 저장
					ceo_menu();
				} else if (user_type == 2) {
					User.verifiedCustomerID = user_ID; //로그인된 사용자 아이디 저장
					cus_menu();
				}else if(user_type == 0) {
					User.verifiedAdminID = user_ID;
					admin_menu();
				}
			}

		} catch (SQLException e) {
			System.out.println("ID 혹은 비밀번호가 일치하지 않습니다");
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}			
	}

	public static void shop_menu(int selectShop) {
		CusRepository CusRep = new CusRepository();
		System.out.println("1 : 메뉴보기, 2 : 후기보기, 3 : 후기작성");
		String cus_ch = sc.nextLine();

		if (cus_ch.equals("1")) {
			//메뉴보기
			MenuController.getMenusByShop(selectShop);

		} else if (cus_ch.equals("2")) {
			//후기보기
			ReviewController.readReaview(selectShop);

		} else if (cus_ch.equals("3")) {
			//후기작성
			ReviewController.insertReview(selectShop);

		}
	}
	//일반고객 페이지
	public static void cus_menu() {
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
			ReviewController.readMyReview();
			

		} else if (cus_ch.equals("4")) {
			//4. 회원정보 수정 메소드 호출
			UserRepository.user_modify();
		}
	}
	public static void ceo_menu() {
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
	
	public static void admin_menu() {
		if(user_type != 0) {
			System.out.println("나가!");
			login();
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


