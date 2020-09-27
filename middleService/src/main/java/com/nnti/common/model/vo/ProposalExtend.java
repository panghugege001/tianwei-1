package com.nnti.common.model.vo;

import java.util.Date;

public class ProposalExtend {
	
	// 提案编号
	private String pno;
	// 存送平台编号
	private String platform;
	// 自助优惠编号
	private Long preferentialId;
	// 手机设备号
	private String sid;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	
	private Integer withdrawType;
	
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Long getPreferentialId() {
		return preferentialId;
	}

	public void setPreferentialId(Long preferentialId) {
		this.preferentialId = preferentialId;
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
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

	public Integer getWithdrawType() {
		return withdrawType;
	}

	public void setWithdrawType(Integer withdrawType) {
		this.withdrawType = withdrawType;
	}	
}