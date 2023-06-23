package com.dto.mangoplate;

public class Menu {
	private int shop_no;
	private int menu_no;
	private String menu_name;
	private int menu_state;
	private String menu_content;
	
	
	public Menu() {
		
	}
	public Menu(int shop_no, int menu_no, String menu_name, String menu_content, int menu_state) {
		super();
		this.shop_no = shop_no;
		this.menu_no = menu_no;
		this.menu_name = menu_name;
		this.menu_state = menu_state;
		this.menu_content = menu_content;
		
	}
	public int getShop_no() {
		return shop_no;
	}
	public void setShop_no(int shop_no) {
		this.shop_no = shop_no;
	}
	public int getMenu_no() {
		return menu_no;
	}
	public void setMenu_no(int menu_no) {
		menu_no++;
		this.menu_no = menu_no;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public int getMenu_state() {
		return menu_state;
	}
	public void setMenu_state(int menu_state) {
		this.menu_state = menu_state;
	}
	public String getMenu_content() {
		return menu_content;
	}
	public void setMenu_content(String menu_content) {
		this.menu_content = menu_content;
	}
	
}
