package com.nnti.pay.controller.vo;

//博士支付
public class DFPayVo {
	
	private String merchantId;
	private String totalAmount;
	private String desc;
	private String corp_flow_no;
	private String sign;
	private String notify_url;

	private String result;
	private String code;
	private String qrCodeURL;
	
	
    public void setOnline_pay(String merchantId,String totalAmount,String desc,String corp_flow_no,String notify_url){
    	this.merchantId = merchantId;
    	this.totalAmount = totalAmount;
    	this.desc = desc;
    	this.corp_flow_no = corp_flow_no;
    	this.notify_url = notify_url;
    }
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCorp_flow_no() {
		return corp_flow_no;
	}
	public void setCorp_flow_no(String corp_flow_no) {
		this.corp_flow_no = corp_flow_no;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getQrCodeURL() {
		return qrCodeURL;
	}
	public void setQrCodeURL(String qrCodeURL) {
		this.qrCodeURL = qrCodeURL;
	}
	
}
