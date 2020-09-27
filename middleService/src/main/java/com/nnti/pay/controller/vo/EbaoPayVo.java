package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * Created by Addis on 2017/7/26.
 */
public class EbaoPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;

	// 商户ID
	private String pay_memberid;

	// 商户订单号
	private String pay_orderid;

	// 订单金额
	private String pay_amount;

	// 订单提交时间
	private String pay_applydate;

	// 银行编码
	private String pay_bankcode;

	// 回调地址
	private String pay_notifyurl;

	// 签名
	private String pay_md5sign;

	/********************* 以下回调通知参数 ************************/

	// 商户ID
	private String memberid;

	// 订单号
	private String orderid;

	// 订单金额
	private String amount;

	// 交易时间
	private String datetime;

	// 交易状态
	private String returncode;
	
	// 自定义参数
	private String pay_reserved1;

	// 验证签名
	private String sign;

	public void setZfbWx(String pay_memberid, String pay_orderid, String pay_amount, String pay_applydate,
			String pay_notifyurl,String pay_reserved1) {
		this.pay_memberid = pay_memberid;
		this.pay_orderid = pay_orderid;
		this.pay_amount = pay_amount;
		this.pay_applydate = pay_applydate;
		this.pay_notifyurl = pay_notifyurl;
		this.pay_reserved1 = pay_reserved1;
	}

	public String getPay_memberid() {
		return pay_memberid;
	}

	public void setPay_memberid(String pay_memberid) {
		this.pay_memberid = pay_memberid;
	}

	public String getPay_orderid() {
		return pay_orderid;
	}

	public void setPay_orderid(String pay_orderid) {
		this.pay_orderid = pay_orderid;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getPay_applydate() {
		return pay_applydate;
	}

	public void setPay_applydate(String pay_applydate) {
		this.pay_applydate = pay_applydate;
	}

	public String getPay_bankcode() {
		return pay_bankcode;
	}

	public void setPay_bankcode(String pay_bankcode) {
		this.pay_bankcode = pay_bankcode;
	}

	public String getPay_notifyurl() {
		return pay_notifyurl;
	}

	public void setPay_notifyurl(String pay_notifyurl) {
		this.pay_notifyurl = pay_notifyurl;
	}

	public String getPay_md5sign() {
		return pay_md5sign;
	}

	public void setPay_md5sign(String pay_md5sign) {
		this.pay_md5sign = pay_md5sign;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getPay_reserved1() {
		return pay_reserved1;
	}

	public void setPay_reserved1(String pay_reserved1) {
		this.pay_reserved1 = pay_reserved1;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
