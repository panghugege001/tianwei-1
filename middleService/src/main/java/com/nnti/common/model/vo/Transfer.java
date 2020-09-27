package com.nnti.common.model.vo;

import java.util.Date;

public class Transfer {

	// 编号
	private Long id;
	// 来源账户
	private String source;
	// 目标账户
	private String target;
	// 转账金额
	private Double remit;
	// 玩家账号
	private String loginName;
	// 创建时间
	private Date createTime;
	// 主账户转账前金额
	private Double credit;
	// 主账户转账后金额
	private Double newCredit;
	//
	private Integer flag;
	//
	private String paymentId;
	// 备注
	private String remark;
	
	public Transfer() {}
	
	public Transfer(Long id, String source, String target, Double remit, String loginName, Date createTime, Double credit, Double newCredit, Integer flag, String remark) {
	
		this.id = id;
		this.source = source;
		this.target = target;
		this.remit = remit;
		this.loginName = loginName;
		this.createTime = createTime;
		this.credit = credit;
		this.newCredit = newCredit;
		this.flag = flag;
		this.remark = remark;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getNewCredit() {
		return newCredit;
	}

	public void setNewCredit(Double newCredit) {
		this.newCredit = newCredit;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}