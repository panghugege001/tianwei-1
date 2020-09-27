package com.nnti.pay.controller.vo;

import java.io.Serializable;

public class FFPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;
	private String MerchantCode;
	private String BankCode;
	private String OrderId;
	private String Amount;
	private String NotifyUrl;
	private String ReturnUrl;
	private String OrderDate;
	private String Ip;
	private String Remark;
	private String Sign;
	
    /*** pc****/
    private String pc;
    
    /*** wap****/
    private String wap;
    
	//通知
	private String OutTradeNo;
	private String Time;
	private String Status; //状态 0处理中，1成功，2失败
	
	
	
    public void setOnline_pay(String MerchantCode, String BankCode, String OrderId,String Amount, String NotifyUrl, String ReturnUrl,
			String OrderDate,String Ip,String Remark) {
    	this.MerchantCode = MerchantCode;
    	this.BankCode = BankCode;
    	this.OrderId = OrderId;
    	this.Amount = Amount;
    	this.NotifyUrl = NotifyUrl;
    	this.ReturnUrl = ReturnUrl;
    	this.OrderDate = OrderDate;
    	this.Ip = Ip;
    	this.Remark = Remark;
	}
	
	
	public String getMerchantCode() {
		return MerchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		MerchantCode = merchantCode;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getNotifyUrl() {
		return NotifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		NotifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return ReturnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		ReturnUrl = returnUrl;
	}
	public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getSign() {
		return Sign;
	}
	public void setSign(String sign) {
		Sign = sign;
	}
	public String getOutTradeNo() {
		return OutTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		OutTradeNo = outTradeNo;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
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
