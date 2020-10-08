package com.gsmc.png.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BBData implements Serializable {

	private static final long serialVersionUID = -6641021778906553154L;

	/**
	 * "Code":"编码",
	 */
	@JsonProperty(value = "Code")
	private String code;

	/**
	 * "Message":"讯息"
	 */
	@JsonProperty(value = "Message")
	private String message;

	/**** 查询会员转帐是否成功 ****/
	/**
	 * "TransID":"转帐序号"
	 */
	@JsonProperty(value = "TransID")
	private String transId;

	/**
	 * "TransType":"转帐型态(入/出)"
	 */
	@JsonProperty(value = "TransType")
	private String transType;

	/**
	 * "Status":"状态(1:成功；-1:处理中或失败 )
	 */
	@JsonProperty(value = "Status")
	private String status;

	/**** 查询会员额度 ****/
	/**
	 * "LoginName":"帐号"
	 */
	@JsonProperty(value = "LoginName")
	private String loginName;

	/**
	 * "Currency":"币别"
	 */
	@JsonProperty(value = "Currency")
	private String currency;

	/**
	 * "Balance":"额度"
	 */
	@JsonProperty(value = "Balance")
	private Double balance;

	/**
	 * "TotalBalance":"总额度"
	 */
	@JsonProperty(value = "TotalBalance")
	private Double totalBalance;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

}

