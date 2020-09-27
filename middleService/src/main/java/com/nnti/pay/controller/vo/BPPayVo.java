package com.nnti.pay.controller.vo;

//博士支付
public class BPPayVo {

    private String MerchantCode;
    private String BankCode;
    private String Amount;//入款金额，单位为分，1元 = 100
    private String OrderId;
    private String NotifyUrl;
    private String ReturnUrl;
    private String OrderDate;
    private String Remark;
    private String Sign; //32位大写MD5签名值
    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
    
    //回调
    private String Status;
    private String OutTradeNo;
    private String Time;
    
    public void setOnline_pay(String MerchantCode, String BankCode, String Amount,String OrderId, String notifyUrl,String ReturnUrl,String OrderDate,
			String Remark) {
    	this.MerchantCode = MerchantCode;
    	this.BankCode = BankCode;
    	this.Amount = Amount;
    	this.OrderId = OrderId;
    	this.NotifyUrl = notifyUrl;
    	this.ReturnUrl = ReturnUrl;
    	this.OrderDate = OrderDate;
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

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
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

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
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
}
