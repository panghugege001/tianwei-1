package com.nnti.pay.controller.vo;

//686支付
public class P686PayVo {

	private String notifyUrl;
	private String sign;
	private String outOrderNo;
	private String goodsClauses;
	private String tradeAmount;
	private String code;
	private String payCode;
    
    //回调
    private String shopCode;
    private String nonStr;
    private String msg;
    
    public void setOnline_pay(String code, String payCode, String tradeAmount,String outOrderNo, String notifyUrl,String goodsClauses) {
    	this.code = code;
    	this.payCode = payCode;
    	this.tradeAmount = tradeAmount;
    	this.outOrderNo = outOrderNo;
    	this.notifyUrl = notifyUrl;
    	this.goodsClauses = goodsClauses;
    	
    }

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getGoodsClauses() {
		return goodsClauses;
	}

	public void setGoodsClauses(String goodsClauses) {
		this.goodsClauses = goodsClauses;
	}

	public String getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getNonStr() {
		return nonStr;
	}

	public void setNonStr(String nonStr) {
		this.nonStr = nonStr;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
