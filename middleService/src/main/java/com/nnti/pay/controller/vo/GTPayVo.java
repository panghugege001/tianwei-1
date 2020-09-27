package com.nnti.pay.controller.vo;


public class GTPayVo {

    /*** 商户 ID */
    private String partner;

    /*** 银行类型*/
    private String banktype;

    /*** 金额 单位元（人民币） */
    private String paymoney;
    /*** 商户系统订单号 */
    private String ordernumber;
    /*** 下行异步通知地址 */
    private String callbackurl;
    /*** 下行同步 通知地址*/
    private String hrefbackurl;
    /*** 备注消息 */
    private String attach;
    /*** MD5 签名 32 位小写 MD5 签名值*/
    private String sign;

    /*** 订单结果 1：支付成功*/
    private String orderstatus;
    /** 高通支付订单号*/
    private String sysnumber;
    
    /*** pc****/
    private String pc;
    
    /*** wap****/
    private String wap;

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

	public void setWxZfb(String merchantCode, String orderId, String orderAmount, String attach, String notifyUrl,
			String shopUrl, String type) {
		this.partner = merchantCode;
		this.ordernumber = orderId;
		this.paymoney = orderAmount;
		this.attach = attach;
		this.callbackurl = notifyUrl;
		this.hrefbackurl = shopUrl;
		this.banktype = type;
	}
}
