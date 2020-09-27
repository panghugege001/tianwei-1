package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 扫呗支付
 */
public class SBPayVo implements Serializable {

    private static final long serialVersionUID = -5704303451746387318L;
    /*** 版本号*/
    private String version;
    /*** 接口名称*/
    private String method;
    /*** 商户号*/
    private String partner;
    
    /*** 银行类型*/
    private String banktype;
    
    /*** 金额*/
    private String paymoney;
    
    /*** 商户订单号*/
    private String ordernumber;
    
    /*** 异步通知地址*/
    private String callbackurl;
    
    /*** 同步通知地址*/
    private String hrefbackurl;
    
    /*** 是否显示收银台*/
    private String isshow;
    
    /*** 备注消息*/
    private String attach;
    
    /*** MD5 签名*/
    private String sign;
    
    

    
    /********以下回调通知参数**************/
    
    /*** 订单结果*/
    private String orderstatus;
    
    
    /*** 扫呗订单号*/
    private String sysnumber;
    

    public SBPayVo() {
    }


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getPartner() {
		return partner;
	}


	public void setPartner(String partner) {
		this.partner = partner;
	}


	public String getBanktype() {
		return banktype;
	}


	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}


	public String getPaymoney() {
		return paymoney;
	}


	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}


	public String getOrdernumber() {
		return ordernumber;
	}


	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}


	public String getCallbackurl() {
		return callbackurl;
	}


	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}


	public String getHrefbackurl() {
		return hrefbackurl;
	}


	public void setHrefbackurl(String hrefbackurl) {
		this.hrefbackurl = hrefbackurl;
	}


	public String getIsshow() {
		return isshow;
	}


	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}


	public String getAttach() {
		return attach;
	}


	public void setAttach(String attach) {
		this.attach = attach;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getOrderstatus() {
		return orderstatus;
	}


	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}


	public String getSysnumber() {
		return sysnumber;
	}


	public void setSysnumber(String sysnumber) {
		this.sysnumber = sysnumber;
	}
    

}
