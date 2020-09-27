package com.nnti.common.model.vo;

import java.util.Date;

public class BigBang {

	//
	private Long id;
	//
	private String userName;
	//
	private Double netWinLose;
	//
	private Date startTime;
	//
	private Date endTime;
	//
	private Date createTime;
	//
	private Double bonus;
	// 状态(0:未派发/1:已派发/2:已领取/3:已处理)
	private String status;
	//
	private Double giftMoney;
	//
	private Integer times;
	// 派发时间
	private Date distributeTime;
	// 领取时间
	private Date getTime;
	// 领取时的投注额
	private Double betAmount;
	// 备注
	private String remark;
	// 派发日期
	private String distributeDay;
	//
	//private Double transferOutAmount;
	// 游戏平台
	private String platform;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getNetWinLose() {
		return netWinLose;
	}

	public void setNetWinLose(Double netWinLose) {
		this.netWinLose = netWinLose;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(Double giftMoney) {
		this.giftMoney = giftMoney;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Date getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(Date distributeTime) {
		this.distributeTime = distributeTime;
	}

	public Date getGetTime() {
		return getTime;
	}

	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDistributeDay() {
		return distributeDay;
	}

	public void setDistributeDay(String distributeDay) {
		this.distributeDay = distributeDay;
	}

	/*public Double getTransferOutAmount() {
		return transferOutAmount;
	}

	public void setTransferOutAmount(Double transferOutAmount) {
		this.transferOutAmount = transferOutAmount;
	}*/

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}