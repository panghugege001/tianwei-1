package com.nnti.personal.model.dto;

import com.nnti.common.model.dto.CommonDTO;

public class AccountTransferDTO extends CommonDTO {

	// 来源账户
	private String source;
	// 目标账户
	private String target;
	// 转账金额
	private Double amount;
	// 优惠券代码
	private String couponCode;
	// 游戏平台
	private String platform;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}