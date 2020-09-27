package com.nnti.common.model.vo;

import java.util.Date;

public class PreferentialRecord {

	// 提案编号
	private String pno;
	// 玩家账号
	private String loginName;
	// 游戏平台
	private String platform;
	// 截止目前的投注额
	private Double validBet;
	// 创建时间
	private Date createTime;
	// 类型(0:禁止/1:放行)
	private Integer type;
	
	public PreferentialRecord() {}
	
	public PreferentialRecord(String pno, String loginName, String platform, Double validBet, Date createTime, Integer type) {
		
		this.pno = pno;
		this.loginName = loginName;
		this.platform = platform;
		this.validBet = validBet;
		this.createTime = createTime;
		this.type = type;
	}
	
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Double getValidBet() {
		return validBet;
	}

	public void setValidBet(Double validBet) {
		this.validBet = validBet;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}