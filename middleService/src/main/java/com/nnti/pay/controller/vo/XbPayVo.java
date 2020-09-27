package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/13.
 */
public class XbPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;
	private String url;
	private String Version;
	private String MerchantCode;
	/*** 我们的订单号 */
	private String OrderId;
	private String Amount;
	private String AsyNotifyUrl;
	private String SynNotifyUrl;
	private String OrderDate;
	private String TradeIp;
	private String PayCode;
	private String Remark1;
	/*** 点卡支付 */
	private String CardNo;
	private String CardPassword;
	/*** 交订单附带的账号信息 */
	private String Remark2;
	private String SignValue;
	/*** 新贝平台交易流水号 */
	private String SerialNo;
	/*** 处理结果 */
	private String State;
	private String FinishTime;

	public void setZfbWx(String merchantCode, String orderAmount, String orderId, String notifyUrl, String shopUrl,
			String orderDate, String signKey, String remark1, String loginName, String customerIp, String apiUrl) {
		this.MerchantCode = merchantCode;
		this.OrderId = orderId;
		this.Amount = orderAmount;
		this.AsyNotifyUrl = notifyUrl;
		this.SynNotifyUrl = shopUrl;
		this.OrderDate = orderDate;
		this.Remark1 = remark1;
		this.Remark2 = loginName;
		this.TradeIp = customerIp;
		this.url = apiUrl;
	}

	public void setOnline(String merchantCode, String payCode, String orderAmount, String orderId, String notifyUrl,
			String shopUrl, String orderDate, String signKey, String remark1, String loginName, String customerIp,
			String apiUrl) {
		this.MerchantCode = merchantCode;
		this.OrderId = orderId;
		this.PayCode = payCode;
		this.Amount = orderAmount;
		this.AsyNotifyUrl = notifyUrl;
		this.SynNotifyUrl = shopUrl;
		this.OrderDate = orderDate;
		this.Remark1 = remark1;
		this.Remark2 = loginName;
		this.TradeIp = customerIp;
		this.url = apiUrl;
	}

	public void setPointCard(String merchantCode, String payCode, String orderAmount, String orderId, String notifyUrl,
			String shopUrl, String orderDate, String signKey, String remark1, String loginName, String customerIp,
			String apiUrl, String cardNo, String cardPassword) {
		this.MerchantCode = merchantCode;
		this.OrderId = orderId;
		this.PayCode = payCode;
		this.Amount = orderAmount;
		this.AsyNotifyUrl = notifyUrl;
		this.SynNotifyUrl = shopUrl;
		this.OrderDate = orderDate;
		this.Remark1 = remark1;
		this.Remark2 = loginName;
		this.TradeIp = customerIp;
		this.url = apiUrl;
		this.CardNo = cardNo;
		this.CardPassword = cardPassword;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getCardPassword() {
		return CardPassword;
	}

	public void setCardPassword(String cardPassword) {
		CardPassword = cardPassword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getMerchantCode() {
		return MerchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		MerchantCode = merchantCode;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getAsyNotifyUrl() {
		return AsyNotifyUrl;
	}

	public void setAsyNotifyUrl(String asyNotifyUrl) {
		AsyNotifyUrl = asyNotifyUrl;
	}

	public String getSynNotifyUrl() {
		return SynNotifyUrl;
	}

	public void setSynNotifyUrl(String synNotifyUrl) {
		SynNotifyUrl = synNotifyUrl;
	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	public String getTradeIp() {
		return TradeIp;
	}

	public void setTradeIp(String tradeIp) {
		TradeIp = tradeIp;
	}

	public String getPayCode() {
		return PayCode;
	}

	public void setPayCode(String payCode) {
		PayCode = payCode;
	}

	public String getRemark1() {
		return Remark1;
	}

	public void setRemark1(String remark1) {
		Remark1 = remark1;
	}

	public String getRemark2() {
		return Remark2;
	}

	public void setRemark2(String remark2) {
		Remark2 = remark2;
	}

	public String getSignValue() {
		return SignValue;
	}

	public void setSignValue(String signValue) {
		SignValue = signValue;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getFinishTime() {
		return FinishTime;
	}

	public void setFinishTime(String finishTime) {
		FinishTime = finishTime;
	}
}
