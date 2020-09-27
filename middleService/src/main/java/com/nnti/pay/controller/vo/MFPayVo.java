package com.nnti.pay.controller.vo;

import java.io.Serializable;

public class MFPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;
	private String merchant_no;
	private String mer_order_no;
	private String order_amount;
	private String pay_type;
	private String notify_url;
	private String return_url;
	private String product_name;
	private String remark;
	private String sign;

	/*** pc ****/
	private String pc;

	/*** wap ****/
	private String wap;

	// 通知
	private String order_status; // 状态 1：待支付 2：已支付

	public void setOnline_pay(String merchant_no, String mer_order_no, String order_amount, String pay_type,
			String notify_url, String return_url, String product_name, String remark) {
		this.merchant_no = merchant_no;
		this.mer_order_no = mer_order_no;
		this.order_amount = order_amount;
		this.pay_type = pay_type;
		this.notify_url = notify_url;
		this.return_url = return_url;
		this.product_name = product_name;
		this.remark = remark;
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getMer_order_no() {
		return mer_order_no;
	}

	public void setMer_order_no(String mer_order_no) {
		this.mer_order_no = mer_order_no;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}

	public String getWap() {
		return wap;
	}

	public void setWap(String wap) {
		this.wap = wap;
	}

}
