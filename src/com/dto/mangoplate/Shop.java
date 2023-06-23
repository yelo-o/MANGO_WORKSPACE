package com.dto.mangoplate;

public class Shop {

	private int shop_no;
	private String shop_name;
	private int shop_state;
	private String shop_content;
	private String shop_type;
	private String ceo_id;

	public Shop() {
		
	}
	
	public Shop(int shop_no, String shop_name, String shop_content, String shop_type, String ceo_id) {
		super();
		this.shop_no = shop_no;
		this.shop_name = shop_name;
		this.shop_content = shop_content;
		this.shop_type = shop_type;
		this.ceo_id = ceo_id;
	}

	public Shop(String shop_name, String shop_content, String shop_type) {
		super();
		this.shop_name = shop_name;
		this.shop_content = shop_content;
		this.shop_type = shop_type;
	}

	public Shop(int shop_no, String shop_name, String shop_content, String shop_type) {
		
		this.shop_no = shop_no;
		this.shop_name = shop_name;
		this.shop_content = shop_content;
		this.shop_type = shop_type;
	}

	public Shop(String shop_name,String shop_content,String shop_type, String ceo_id) {
		this.shop_content = shop_content;
		this.shop_name = shop_name;
		this.shop_type = shop_type;
		this.ceo_id = ceo_id;
	}
	public Shop(int shop_no, String shop_name, int shop_state, String shop_type, String ceo_id) {
		super();
		this.shop_no = shop_no;
		this.shop_name = shop_name;
		this.shop_state = shop_state;
		this.shop_type = shop_type;
		this.ceo_id = ceo_id;
	}
	
	public Shop(int shop_no, String shop_name, int shop_state, String shop_content, String shop_type,
			String ceo_id) {
		this.shop_no = shop_no;
		this.shop_name = shop_name;
		this.shop_content = shop_content;
		this.shop_state = shop_state;
		this.shop_type = shop_type;
		this.ceo_id = ceo_id;
	}

	public int getShop_no() {
		return shop_no;
	}
	public void setShop_no(int shop_no) {
		shop_no++;
		this.shop_no = shop_no;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public int getShop_state() {
		return shop_state;
	}
	public void setShop_state(int shop_state) {
		this.shop_state = shop_state;
	}
	public String getShop_content() {
		return shop_content;
	}
	public void setShop_content(String shop_content) {
		this.shop_content = shop_content;
	}
	public String getShop_type() {
		return shop_type;
	}
	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}
	public String getCeo_id() {
		return ceo_id;
	}
	public void setCeo_id(String ceo_id) {
		this.ceo_id = ceo_id;
	}
	
}
