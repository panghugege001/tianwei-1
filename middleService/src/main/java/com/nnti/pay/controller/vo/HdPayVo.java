package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 讯联宝微信和支付宝支付提交网关参数
 */
public class HdPayVo implements Serializable {

    private static final long serialVersionUID = 7324634869954607545L;
    /*** 接口名字*/
    private String apiName;
    /*** 接口版本*/
    private String apiVersion;
    /*** 平台ID*/
    private String platformID;
    /*** 商户号*/
    private String merchNo;
    /*** 商户订单号*/
    private String orderNo;
    /*** 交易时间 格式：yyyyMMdd*/
    private String tradeDate;
    /*** 充值金额*/
    private String amt;
    /*** 第三方回掉地址 */
    private String merchUrl;

    /*** 支付时上送的商户参数*/
    private String merchParam;

    private String tradeSummary;
    
    private String choosePayType;
    /*** 超时时间 */
    private String overTime;
    /*** 客户请求IP */
    private String customerIP;
    /*** 签名*/
    private String signMsg;

    /*** 通知时间*/
    private String notifyTime;

    /*** 实际的支付金额   */
    private String tradeAmt;

    /*** 支付平台订单号 */
    private String accNo;

    /*** 支付平台订单支付日期 */
    private String accDate;

    /*** 处理结果0 未支付，1 成功，2失败 */
    private String orderStatus;

    /*** 服务器，商户需回写应答。*/
    private String notifyType;

    private String url;
    
    private String apiUrl;

    private String bankCode;

    public HdPayVo() {
    }

    public HdPayVo(String apiName, String apiVersion, String platformID, String merchNo, String orderNo, String tradeDate, String amt, String merchUrl, String merchParam, String tradeSummary, String overTime, String customerIP, String signMsg) {
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        this.platformID = platformID;
        this.merchNo = merchNo;
        this.orderNo = orderNo;
        this.tradeDate = tradeDate;
        this.amt = amt;
        this.merchUrl = merchUrl;
        this.merchParam = merchParam;
        this.tradeSummary = tradeSummary;
        this.overTime = overTime;
        this.customerIP = customerIP;
        this.signMsg = signMsg;
    }

    /*** 设置支付宝或微信接口属性值*/
    public void setWxZfb(String platformID, String merchNo, String orderId, String tradeDate, String amt, String merchUrl, String merchParam, String customerIp) {
        this.platformID = platformID;
        this.merchNo = merchNo;
        this.orderNo = orderId;
        this.tradeDate = tradeDate;
        this.amt = amt;
        this.merchUrl = merchUrl;
        this.merchParam = merchParam;
        this.customerIP = customerIp;
    }

    public void setOnline(String platformID, String merchNo, String orderNo, String tradeDate, String amt, String merchUrl, String merchParam, String bankCode,String customerIP) {
        this.bankCode = bankCode;
        setWxZfb(platformID, merchNo, orderNo, tradeDate, amt, merchUrl, merchParam, customerIP);
    }
    
    /****  查询订单状态 构造方法*/
    public void setQueryOrder(String merchNo,String apiVersion,String tradeAmt,String apiName){
    	 this.platformID = merchNo;
         this.apiVersion = apiVersion;
         this.amt = tradeAmt;
         this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public String getMerchNo() {
        return merchNo;
    }

    public void setMerchNo(String merchNo) {
        this.merchNo = merchNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getMerchUrl() {
        return merchUrl;
    }

    public void setMerchUrl(String merchUrl) {
        this.merchUrl = merchUrl;
    }

    public String getMerchParam() {
        return merchParam;
    }

    public void setMerchParam(String merchParam) {
        this.merchParam = merchParam;
    }

    public String getTradeSummary() {
        return tradeSummary;
    }

    public void setTradeSummary(String tradeSummary) {
        this.tradeSummary = tradeSummary;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getCustomerIP() {
        return customerIP;
    }

    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(String tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccDate() {
        return accDate;
    }

    public void setAccDate(String accDate) {
        this.accDate = accDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

	public String getChoosePayType() {
		return choosePayType;
	}

	public void setChoosePayType(String choosePayType) {
		this.choosePayType = choosePayType;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	
    
    
}
