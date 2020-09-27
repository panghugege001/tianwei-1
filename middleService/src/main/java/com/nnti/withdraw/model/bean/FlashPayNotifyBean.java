package com.nnti.withdraw.model.bean;

public class FlashPayNotifyBean {
	private String order_no;
	private String order_status;
	private String fail_message;
	private String card_number;
	private String card_balance ;
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getFail_message() {
		return fail_message;
	}
	public void setFail_message(String fail_message) {
		this.fail_message = fail_message;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getCard_balance() {
		return card_balance;
	}
	public void setCard_balance(String card_balance) {
		this.card_balance = card_balance;
	}
	

}
