package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/2/13.
 */
public class HcPayVo {

    private String MerNo;

    private String BillNo;

    private String Amount;

    private String ReturnURL;
    private String AdviceURL;
    private String SignInfo;
    private String Remark;
    private String defaultBankNumber;
    private String payType;
    private String OrderTime;
    private String products;
    private String OrderNo;
    private String tradeOrder;
    private String Succeed;
    private String Result;
    private String baseXml;
    
    private String AdviceUrl; 

    public void setOnline(String merchantCode, String orderId, String orderAmount, String tradeDate, String notifyUrl, String shopUrl, String remark, String products) {
        this.MerNo = merchantCode;
        this.BillNo = orderId;
        this.Amount = orderAmount;
        this.OrderTime = tradeDate;
        this.ReturnURL = shopUrl;
        this.AdviceURL = notifyUrl;
        this.Remark = remark;
        this.products = products;
    }
    
    
    public void setWx_zfb(String merchantCode, String orderId, String orderAmount, String tradeDate, String notifyUrl, String shopUrl, String remark, String products) {
        this.MerNo = merchantCode;
        this.BillNo = orderId;
        this.Amount = orderAmount;
        this.OrderTime = tradeDate;
        this.ReturnURL = shopUrl;
        this.AdviceUrl = notifyUrl;
        this.Remark = remark;
        this.products = products;
    }


    public String getMerNo() {
        return MerNo;
    }

    public void setMerNo(String merNo) {
        MerNo = merNo;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getReturnURL() {
        return ReturnURL;
    }

    public void setReturnURL(String returnURL) {
        ReturnURL = returnURL;
    }

    public String getAdviceURL() {
        return AdviceURL;
    }

    public void setAdviceURL(String adviceURL) {
        AdviceURL = adviceURL;
    }

    public String getSignInfo() {
        return SignInfo;
    }

    public void setSignInfo(String signInfo) {
        SignInfo = signInfo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDefaultBankNumber() {
        return defaultBankNumber;
    }

    public void setDefaultBankNumber(String defaultBankNumber) {
        this.defaultBankNumber = defaultBankNumber;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.OrderTime = orderTime;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getTradeOrder() {
        return tradeOrder;
    }

    public void setTradeOrder(String tradeOrder) {
        this.tradeOrder = tradeOrder;
    }

    public String getSucceed() {
        return Succeed;
    }

    public void setSucceed(String succeed) {
        Succeed = succeed;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

	public String getBaseXml() {
		return baseXml;
	}

	public void setBaseXml(String baseXml) {
		this.baseXml = baseXml;
	}

	public String getAdviceUrl() {
		return AdviceUrl;
	}

	public void setAdviceUrl(String adviceUrl) {
		AdviceUrl = adviceUrl;
	}
	
	
    
}
