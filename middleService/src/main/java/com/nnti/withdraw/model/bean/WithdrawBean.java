package com.nnti.withdraw.model.bean;

public class WithdrawBean {
	private Double amount ; 
	private String orderNo ;
	public WithdrawBean() {
		super();
	}
	public WithdrawBean(Double amount, String orderNo) {
		super();
		this.amount = amount;
		this.orderNo = orderNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	} 
	
}
