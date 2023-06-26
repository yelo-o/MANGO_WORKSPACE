
package com.main.mangoplate;

import java.util.Scanner;



import com.repository.mangoplate.UserRepository;



public class MangoPlate {

   @SuppressWarnings("static-access")
   public static void main(String[] args) throws ClassNotFoundException {
      MangoPlate mango = new MangoPlate();
      Scanner sc = new Scanner(System.in);
      String login_ch;
      String register_ch;
      do {
         System.out.println("안녕하세요! 망고 플레이트 입니다! - ver.Console");
         System.out.println("1. 로그인 ----------- 2. 회원가입");
         login_ch = sc.nextLine();
         //로그인
         if(login_ch.equals("1")) {
            UserRepository.login();
         }//가입
         else if(login_ch.equals("2")) {
         
            System.out.println("1 : 일반 고객  2 : 점주 고객");
            register_ch = sc.nextLine();
            
            if(register_ch.equals("1")) {
            	//일반고객 usertype=2
                UserRepository.register(2);
            }	
            else if(register_ch.equals("2")) {
               //점주 고객 usertype=1
            	UserRepository.register(1);
            }
            else if(register_ch.equals("iamadmin")) {
               //관리자 usertype=2=0
            	UserRepository.register(0);
            }
            mango.main(args);
         }
      }while(!login_ch.equals("9"));

      sc.close();
   }

}