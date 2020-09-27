package com.nnti.personal.model.vo;

import java.util.Date;

public class RedEnvelopeActivity {

	// 编号
	private Integer id;
	// 活动标题
	private String title;
	// 活动最小红利
	private Double minBonus;
	// 活动最大红利
	private Double maxBonus;
	// 等级
	private String vip;
	// 领取次数(单个账号)
	private Integer times;
	// 领取次数(总数量)
	private Integer maxReceiveTimes;
	// 转入平台编号
	private String platformId;
	// 转入平台名称
	private String platformName;
	// 流水倍数
	private Integer multiples;
	// 存款额
	private Double depositAmount;
	// 存款开始时间
	private Date depositStartTime;
	// 存款结束时间
	private Date depositEndTime;
	// 投注额
	private Double betAmount;
	// 投注开始时间
	private Date betStartTime;
	// 投注结束时间
	private Date betEndTime;
	// 活动开始时间
	private Date startTime;
	// 活动结束时间
	private Date endTime;
	// 删除标志，Y:已删除/N:未删除
	private String deleteFlag;
	// 创建人
	private String createUser;
	// 创建时间
	private Date createTime;
	// 修改人
	private String updateUser;
	// 修改时间
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getMinBonus() {
		return minBonus;
	}

	public void setMinBonus(Double minBonus) {
		this.minBonus = minBonus;
	}

	public Double getMaxBonus() {
		return maxBonus;
	}

	public void setMaxBonus(Double maxBonus) {
		this.maxBonus = maxBonus;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getMaxReceiveTimes() {
		return maxReceiveTimes;
	}

	public void setMaxReceiveTimes(Integer maxReceiveTimes) {
		this.maxReceiveTimes = maxReceiveTimes;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Integer getMultiples() {
		return multiples;
	}

	public void setMultiples(Integer multiples) {
		this.multiples = multiples;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Date getDepositStartTime() {
		return depositStartTime;
	}

	public void setDepositStartTime(Date depositStartTime) {
		this.depositStartTime = depositStartTime;
	}

	public Date getDepositEndTime() {
		return depositEndTime;
	}

	public void setDepositEndTime(Date depositEndTime) {
		this.depositEndTime = depositEndTime;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	public Date getBetStartTime() {
		return betStartTime;
	}

	public void setBetStartTime(Date betStartTime) {
		this.betStartTime = betStartTime;
	}

	public Date getBetEndTime() {
		return betEndTime;
	}

	public void setBetEndTime(Date betEndTime) {
		this.betEndTime = betEndTime;
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

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}