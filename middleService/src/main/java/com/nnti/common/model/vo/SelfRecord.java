package com.nnti.common.model.vo;

import java.util.Date;

public class SelfRecord {
	
	// 提案编号
	private String pno;
	// 玩家账号
	private String loginName;
	// 游戏平台
	private String platform;
	// 自助优惠名称
	private String selfName;
	// 是否达到流水标志(0:流水未达到/1:流水已达到)
	private Integer type;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 备注
	private String remark;
	
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

	public String getSelfName() {
		return selfName;
	}

	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}