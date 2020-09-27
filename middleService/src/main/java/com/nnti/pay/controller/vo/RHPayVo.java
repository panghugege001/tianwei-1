package com.nnti.pay.controller.vo;

/**
 * Created by pony on 2018/06/09.
 */
public class RHPayVo {

    private String merNo;
    private String orderNo;
    private String amount;
    private String returnUrl;
    private String notifyUrl;
    private String bankId;
    private String remark;
    private String sign;
    
    private String pc;
    private String wap;
    
    private String trxNo; 
    private String status;

    public RHPayVo() {
    }

    //*** 支付宝 微信参数值*//*
    public void setOnlinePay(String merNo, String orderNo, String amount, String returnUrl, String notifyUrl, String remark, String bankId) {
        this.merNo = merNo;
        this.orderNo = orderNo;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.notifyUrl = notifyUrl;
        this.remark = remark;
        this.bankId = bankId;
    }

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
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

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
