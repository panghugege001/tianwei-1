package com.nnti.pay.controller.vo;

/**
 * Created by Addis on 2017/4/28.
 */
public class OnePayVo {

	// 商户号
	private String ag_account;
	// 金额
	private String amount;
	// 商户订单号
	private String order_no;
	// 支付时间
	private String pay_time;
	// 客户端ip
	private String pay_ip;
	// 签名
	private String sign;
	// key
	private String key;
	// 签名类型
	private String sign_type;
	//支付种类
	private String order_type;

	// 自定义参数
	private String attach;
	/** 通知返回参数 ******/
	private String status;

	public void setWxZfb(String ag_account, String amount, String order_no, String pay_time, String pay_ip, String sign,
			String attach) {
		this.ag_account = ag_account;
		this.amount = amount;
		this.order_no = order_no;
		this.pay_time = pay_time;
		this.pay_ip = pay_ip;
		this.sign = sign;
		this.attach = attach;
	}

	public String getAg_account() {
		return ag_account;
	}

	public void setAg_account(String ag_account) {
		this.ag_account = ag_account;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getPay_ip() {
		return pay_ip;
	}

	public void setPay_ip(String pay_ip) {
		this.pay_ip = pay_ip;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	

}
