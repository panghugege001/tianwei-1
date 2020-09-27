package com.nnti.pay.controller.vo;


public class WFPayVo {
	
	private String svcName;
	private String merId;
	private String merchOrderId;
	private String tranType;
	private String pName;
	private String amt;
	private String cardNo;
	private String cardPwd;
	private String cardAmount;
	private String notifyUrl;
	private String retUrl;
	private String showCashier;
	private String merData;
	private String md5value;

    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
	
	private String status;
	private String orderStatusMsg;
	private String orderId;
	private String tranTime;
	
    public void setOnline_pay(String svcName, String merId, String merchOrderId,String tranType, String pName,String amt,String cardNo,
			String cardPwd,String cardAmount, String notifyUrl,String retUrl,String showCashier,String merData) {
		this.svcName = svcName;
		this.merId = merId;
		this.merchOrderId = merchOrderId;
		this.tranType = tranType;
		this.pName = pName;
		this.amt = amt;
		this.cardNo = cardNo;
		this.cardPwd = cardPwd;
		this.cardAmount = cardAmount;
		this.notifyUrl = notifyUrl;
		this.retUrl = retUrl;
		this.showCashier = showCashier;
		this.merData = merData;    
	}
	
	public String getSvcName() {
		return svcName;
	}
	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getMerchOrderId() {
		return merchOrderId;
	}
	public void setMerchOrderId(String merchOrderId) {
		this.merchOrderId = merchOrderId;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getPName() {
		return pName;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardPwd() {
		return cardPwd;
	}
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	public String getCardAmount() {
		return cardAmount;
	}
	public void setCardAmount(String cardAmount) {
		this.cardAmount = cardAmount;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getRetUrl() {
		return retUrl;
	}
	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	public String getShowCashier() {
		return showCashier;
	}
	public void setShowCashier(String showCashier) {
		this.showCashier = showCashier;
	}
	public String getMerData() {
		return merData;
	}
	public void setMerData(String merData) {
		this.merData = merData;
	}
	public String getMd5value() {
		return md5value;
	}
	public void setMd5value(String md5value) {
		this.md5value = md5value;
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
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderStatusMsg() {
		return orderStatusMsg;
	}
	public void setOrderStatusMsg(String orderStatusMsg) {
		this.orderStatusMsg = orderStatusMsg;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	
}
