package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 神奇支付提交网关参数
 */
public class SQPayVo implements Serializable {

    private static final long serialVersionUID = 7324634869954607545L;
    /*** 商户支付key*/
    private String payKey;
    /*** 订单金额 元*/
    private String orderPrice;
    /*** 商户支付订单号*/
    private String outTradeNo;
    /*** 产品类型*/
    private String productType;
    /*** 下单时间*/
    private String orderTime;
    /***支付产品名称*/
    private String productName;
    /*** 下单ip*/
    private String orderIp;
    /*** 银行编码 */
    private String bankCode;

    /*** 支付银行卡类型*/
    private String bankAccountType;

    /*** 页面通知地址*/
    private String returnUrl;
    /*** notifyUrl*/
    private String notifyUrl;
    /*** 备注 */
    private String remark;
    /*** 移动端（当为手机端时此参数不为空 值为 1）*/
    private String mobile;
    /*** 签名*/
    private String sign;

    
    private String apiUrl;

    public SQPayVo() {
    }


    
    
    public SQPayVo(String payKey, String orderPrice, String outTradeNo, String productType, String orderTime,
			String productName, String orderIp, String bankCode, String bankAccountType, String returnUrl,
			String notifyUrl, String remark, String mobile, String sign, String apiUrl) {
		super();
		this.payKey = payKey;
		this.orderPrice = orderPrice;
		this.outTradeNo = outTradeNo;
		this.productType = productType;
		this.orderTime = orderTime;
		this.productName = productName;
		this.orderIp = orderIp;
		this.bankCode = bankCode;
		this.bankAccountType = bankAccountType;
		this.returnUrl = returnUrl;
		this.notifyUrl = notifyUrl;
		this.remark = remark;
		this.mobile = mobile;
		this.sign = sign;
		this.apiUrl = apiUrl;
	}

    

    /*** 设置支付宝或微信接口属性值*/
    public void setWxZfb(String orderPrice, String outTradeNo, String orderTime, String productName, String orderIp, String bankCode, String returnUrl, String notifyUrl, String remark,String paykey) {
       this.orderPrice = orderPrice;
       this.outTradeNo = outTradeNo;
       this.orderTime = orderTime;
       this.productName = productName;
       this.orderIp = orderIp;
       this.bankCode = bankCode;
       this.returnUrl = returnUrl;
       this.notifyUrl = notifyUrl;
       this.remark = remark;
       this.payKey = paykey;
    }
    



	public String getPayKey() {
		return payKey;
	}




	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}




	public String getOrderPrice() {
		return orderPrice;
	}




	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}




	public String getOutTradeNo() {
		return outTradeNo;
	}




	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}




	public String getProductType() {
		return productType;
	}




	public void setProductType(String productType) {
		this.productType = productType;
	}




	public String getOrderTime() {
		return orderTime;
	}




	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}




	public String getProductName() {
		return productName;
	}




	public void setProductName(String productName) {
		this.productName = productName;
	}




	public String getOrderIp() {
		return orderIp;
	}




	public void setOrderIp(String orderIp) {
		this.orderIp = orderIp;
	}




	public String getBankCode() {
		return bankCode;
	}




	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}




	public String getBankAccountType() {
		return bankAccountType;
	}




	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
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




	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}




	public String getMobile() {
		return mobile;
	}




	public void setMobile(String mobile) {
		this.mobile = mobile;
	}




	public String getSign() {
		return sign;
	}




	public void setSign(String sign) {
		this.sign = sign;
	}




	public String getApiUrl() {
		return apiUrl;
	}




	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}




    
}
