package com.nnti.personal.model.dto;

import com.nnti.common.model.dto.CommonDTO;

public class SelfDepositDTO extends CommonDTO {
	
	// 存送优惠平台
	private String platform;
	// 存送优惠编号
	private String id;
	// 存送优惠类型
	private String type;
	// 转账金额
	private Double amount;
	// 申请渠道
	private String channel;
	// 标记
	private String sign;
	// 存储条件类型
	private String conditionType;
		
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getId() {
		return id;
	}
		
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
}