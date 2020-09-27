package com.nnti.pay.controller.vo;

public class TTPayVO {

	private String merchantNo;//商户平台订单号
	private String orderTime;//商户订单提交时间，格式yyyyMMddHHmmss
	private String customerOrderNo;//商户订单号，唯一
	private String amount;//单位：分
	private String subject;//商品名称
	private String body;//商品描述
	private String payerIp;//付款用户的IP地址
	private String payerAccountNo;//付款用户的银行卡卡号(银联扫码此字段不需要)
	private String notifyUrl;//商户接收付款成功通知的地址
	private String pageUrl;//付款成功后商户页面跳转地址
	private String channel;//银行编码，请参考银行编码，银联云闪付H5填：UNIONPAY
	private String payType;//固定值，请参考支付方式编码，银联云闪付H5填：UnionpayH5，扫码：UnionpayScan（信息传递中暂不支持中文）
	private String signType;//固定值：MD5
	private String sign;//签名字符串
	
	private String orderNo;
	private String status;
	private String code;

    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
    
    public void setOnline_pay(String merchantNo,String orderTime,String customerOrderNo,String amount,String subject,String body,
    		String payerIp,String payerAccountNo,String payType,String notifyUrl,String pageUrl,String signType){
    	this.merchantNo = merchantNo;
    	this.orderTime = orderTime;
    	this.customerOrderNo = customerOrderNo;
    	this.amount = amount;
    	this.subject = subject;
    	this.body = body;
    	this.payerIp = payerIp;
    	this.payerAccountNo = payerAccountNo;
    	this.payType = payType;
    	this.notifyUrl = notifyUrl;
    	this.pageUrl = pageUrl;
    	this.signType = signType;
    }
    
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getCustomerOrderNo() {
		return customerOrderNo;
	}
	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPayerIp() {
		return payerIp;
	}
	public void setPayerIp(String payerIp) {
		this.payerIp = payerIp;
	}
	public String getPayerAccountNo() {
		return payerAccountNo;
	}
	public void setPayerAccountNo(String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
