package com.nnti.common.model.vo;

public class UserStatus {
	
	// 玩家登录账号
	private String loginName;
	// 登录错误次数
	private Integer loginErrorNum;     
	// 老虎机佣金总额
	private Double slotAccount;
	// 是否有评论权限(0:否/1:是)
	private Integer discussFlag;
	// 代理佣金比例
	private String commission;
	//用于生成订单唯一号
	private String payOrderValue;
	//
	private Integer cashInWrong;
	//
	private Integer touZhuFlag;
	// 是否通过短信反向验证，0:否/1:是
	private String smsFlag;
	//邮件标识
	private String mailFlag;
	
	//短信验证码
	private String validateCode;
	
	
	
	public String getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public Integer getCashInWrong() {
		return cashInWrong;
	}

	public void setCashInWrong(Integer cashInWrong) {
		this.cashInWrong = cashInWrong;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getLoginErrorNum() {
		return loginErrorNum;
	}

	public void setLoginErrorNum(Integer loginErrorNum) {
		this.loginErrorNum = loginErrorNum;
	}

	public Double getSlotAccount() {
		return slotAccount;
	}

	public void setSlotAccount(Double slotAccount) {
		this.slotAccount = slotAccount;
	}

	public Integer getDiscussFlag() {
		return discussFlag;
	}

	public void setDiscussFlag(Integer discussFlag) {
		this.discussFlag = discussFlag;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getPayOrderValue() {
		return payOrderValue;
	}

	public void setPayOrderValue(String payOrderValue) {
		this.payOrderValue = payOrderValue;
	}

	public Integer getTouZhuFlag() {
		return touZhuFlag;
	}

	public void setTouZhuFlag(Integer touZhuFlag) {
		this.touZhuFlag = touZhuFlag;
	}
}