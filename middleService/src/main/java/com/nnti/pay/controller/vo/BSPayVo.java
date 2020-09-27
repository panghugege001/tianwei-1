package com.nnti.pay.controller.vo;

//百盛支付
public class BSPayVo {

    private String MerchantId;
    private String Sign;
    private String Timestamp;
    private String PaymentTypeCode;
    private String OutPaymentNo;
    private String PaymentAmount;//入款金额，单位为分，1元 = 100
    private String NotifyUrl;
    private String PassbackParams;
    /*** pc****/
    private String pc;
    
    /*** wap****/
    private String wap;
    //回调
    private String code;
    private String message;
    private String paymentNo;
    private String paymentFee;
    private String paymentState;
    
    public void setOnline_pay(String merchantId, String timestamp, String paymentTypeCode,String outPaymentNo, String paymentAmount, String notifyUrl,
			String passbackParams) {
    	this.MerchantId = merchantId;
    	this.Timestamp = timestamp;
    	this.PaymentTypeCode = paymentTypeCode;
    	this.OutPaymentNo = outPaymentNo;
    	this.PaymentAmount = paymentAmount;
    	this.NotifyUrl = notifyUrl;
    	this.PassbackParams = passbackParams;
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


	public String getMerchantId() {
		return MerchantId;
	}


	public void setMerchantId(String merchantId) {
		MerchantId = merchantId;
	}


	public String getSign() {
		return Sign;
	}


	public void setSign(String sign) {
		Sign = sign;
	}


	public String getTimestamp() {
		return Timestamp;
	}


	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}


	public String getPaymentTypeCode() {
		return PaymentTypeCode;
	}


	public void setPaymentTypeCode(String paymentTypeCode) {
		PaymentTypeCode = paymentTypeCode;
	}


	public String getOutPaymentNo() {
		return OutPaymentNo;
	}


	public void setOutPaymentNo(String outPaymentNo) {
		OutPaymentNo = outPaymentNo;
	}


	public String getPaymentAmount() {
		return PaymentAmount;
	}


	public void setPaymentAmount(String paymentAmount) {
		PaymentAmount = paymentAmount;
	}


	public String getNotifyUrl() {
		return NotifyUrl;
	}


	public void setNotifyUrl(String notifyUrl) {
		NotifyUrl = notifyUrl;
	}


	public String getPassbackParams() {
		return PassbackParams;
	}


	public void setPassbackParams(String passbackParams) {
		PassbackParams = passbackParams;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public String getPaymentFee() {
		return paymentFee;
	}
	public void setPaymentFee(String paymentFee) {
		this.paymentFee = paymentFee;
	}
	public String getPaymentState() {
		return paymentState;
	}
	public void setPaymentState(String paymentState) {
		this.paymentState = paymentState;
	}
	
    
}
