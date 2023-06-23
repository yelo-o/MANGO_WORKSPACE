package com.main.mangoplate;

import java.util.Scanner;

import com.dao.mangoplate.shopController;
import com.dao.mangoplate.userController;

public class MangoPlate {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MangoPlate mango = new MangoPlate();
		shopController ceo = new shopController();
		userController user = new userController();
		//      ReviewController reviewCon = new ReviewController(); 
		Scanner sc = new Scanner(System.in);
		String func_ch,ceo_ch,cus_ch,adin_ch,login_ch;
		String register_ch;
		do {
			System.out.println("안녕하세요! 망고 플레이트 입니다! - ver.Console");
			System.out.println("1. 로그인 ----------- 2. 회원가입");
			login_ch = sc.nextLine();
			//로그인
			if(login_ch.equals("1")) {
				user.login();

			}//가입
			else if(login_ch.equals("2")) {

				System.out.println("1 : 점주 고객  2 : 일반 고객");
				register_ch = sc.nextLine();

				if(register_ch.equals("1")) {
					//점주 고객
					//               user_register.ceo_register();
				}
				else if(register_ch.equals("2")) {
					userController.user_register();
				}
				else if(register_ch.equals("iamadmin")) {
					//관리자
					//               user_register.admin_register();
				}
				mango.main(args);
			}
		}while(!login_ch.equals("9"));

	}

}