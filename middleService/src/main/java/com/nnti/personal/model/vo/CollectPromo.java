package com.nnti.personal.model.vo;

import java.util.Date;

public class CollectPromo {

	// 提案号
	private String pno;
	// 用户名
	private String userName;
	// 输赢额
	private Double amount;
	// 提款所需流水倍数
	private Integer times;
	// 状态(0:未领取/1:已领取/2:已处理/3:已取消)
	private String status;
	// 已投注额(领取优惠时游戏总投注额)
	private Double betting;
	// 创建时间
	private Date createTime;
	// 游戏平台
	private String platform;
	// 备注
	private String remark;
	//
	private Integer collectId;

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getBetting() {
		return betting;
	}

	public void setBetting(Double betting) {
		this.betting = betting;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}
}