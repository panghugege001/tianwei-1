package com.nnti.common.model.vo;

import java.util.Date;

public class PTProfit {

	//
	private String uuid;
	//
	private Long userId;
	//
	private Double amount;
	//
	private Double betCredit;
	//
	private Date startTime;
	//
	private Date endTime;
	//
	private String loginName;
	//
	private Double payOut;
	//
	private Date updateTime;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBetCredit() {
		return betCredit;
	}

	public void setBetCredit(Double betCredit) {
		this.betCredit = betCredit;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Double getPayOut() {
		return payOut;
	}

	public void setPayOut(Double payOut) {
		this.payOut = payOut;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}