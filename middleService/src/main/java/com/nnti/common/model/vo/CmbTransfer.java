package com.nnti.common.model.vo;

import java.util.Date;

public class CmbTransfer {

	// 编号
	private Integer transferId;
	// 充值金额
	private Double amount;
	// 参数余额
	private Double balance;
	// 交易类型
	private String jylx;
	// 附言
	private String notes;
	// 存款人姓名
	private String acceptName;
	// 存款人卡号
	private String acceptCardNum;
	// 付款时间
	private Date payDate;
	//
	private Integer adminId;
	// 状态(0:未处理/1:已充值/2:充值超时处理)
	private Integer status;
	// 数据创建时间
	private Date date;
	// 匹配用时
	private String timeCha;
	//
	private Integer overTime;
	// 持卡人姓名
	private String userAccountName;
	
	// 持卡人卡卡号
	private String uaccountno;
	// 玩家账号
	private String loginName;
	// 备注
	private String remark;
	// 支付类型(0:网银/1:支付宝/2:微信/3:通联)
	private Integer payType;
	// 单号
	private String orderNumber;
	
	public Integer getTransferId() {
		return transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getJylx() {
		return jylx;
	}

	public void setJylx(String jylx) {
		this.jylx = jylx;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAcceptName() {
		return acceptName;
	}

	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}

	public String getAcceptCardNum() {
		return acceptCardNum;
	}

	public void setAcceptCardNum(String acceptCardNum) {
		this.acceptCardNum = acceptCardNum;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeCha() {
		return timeCha;
	}

	public void setTimeCha(String timeCha) {
		this.timeCha = timeCha;
	}

	public Integer getOverTime() {
		return overTime;
	}

	public void setOverTime(Integer overTime) {
		this.overTime = overTime;
	}

	public String getUserAccountName() {
		return userAccountName;
	}

	public void setUserAccountName(String userAccountName) {
		this.userAccountName = userAccountName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getUaccountno() {
		return uaccountno;
	}

	public void setUaccountno(String uaccountno) {
		this.uaccountno = uaccountno;
	}
	
	
}