package com.nnti.pay.controller.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TCPayVO {

	// 商户编号
	private String merchant;
	// 收款方式
	private String qrtype;
	// 商户订单号
	private String customno;
	// 支付金额
	private String money;
	// 时间戳
	private String sendtime;
	// 通知地址
	private String notifyurl;
	private String backurl;
	private String risklevel; // 固定值数字1-5， 系统将根据风险级别分配相应的收款号给客户，如果空表示不限制风险级别。
	// 签名串
	private String sign;
	
    /*** pc****/
    private String pc;
    
    /*** wap****/
    private String wap;

	// 系统订单号
	private String orderno;

	private String paytime;
	// 付值状态
	private String state;

	/*** 提交接口参数值 */
	public void setOnline_pay(String merchant, String qrtype, String customno, String money, String sendtime,
			String notifyurl, String backurl) {
		this.merchant = merchant;
		this.qrtype = qrtype;
		this.customno = customno;
		this.money = money;
		this.sendtime = sendtime;
		this.notifyurl = notifyurl;
		this.backurl = backurl;

	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getQrtype() {
		return qrtype;
	}

	public void setQrtype(String qrtype) {
		this.qrtype = qrtype;
	}

	public String getCustomno() {
		return customno;
	}

	public void setCustomno(String customno) {
		this.customno = customno;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getRisklevel() {
		return risklevel;
	}

	public void setRisklevel(String risklevel) {
		this.risklevel = risklevel;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
}
