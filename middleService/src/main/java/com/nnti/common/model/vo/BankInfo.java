package com.nnti.common.model.vo;

import java.util.Date;
import java.util.List;

public class BankInfo {

	// 编号
	private Long id;
	// 银行名称
	private String bankName;
	// 玩家姓名
	private String userName;
	//
	private Double amount;
	//
	private Integer useable;
	//
	private Integer type;
	private List<Integer> types;
	//
	private Integer isShow;
	//
	private String userRole;
	//
	private String zfbImgCode;
	//
	private Integer scanAccount;
	//
	private String vpnName;
	//
	private String vpnPassword;
	//
	private String bankCard;
	//
	private String accountNo;
	//
	private String remark;
	//
	private String loginName;
	//
	private String password;
	//
	private Integer bankType;
	//
	private String usb;
	//
	private String realName;
	//
	private Date updateTime;
	//
	private Double bankAmount;
	//
	private String remoteIp;
	//
	private Integer isTransfer;
	//
	private Integer isActive;
	//
	private Integer transferSwitch;
	//
	private Integer sameBankSwitch;
	//
	private String sameBank;
	//
	private String crossBank;
	//
	private Double transferMoney;
	//
	private Integer autoPay;
	//
	private Double fee;

	private double depositMin = 0.0;

	private double depositMax = 0.0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public Integer getUseable() {
		return useable;
	}

	public void setUseable(Integer useable) {
		this.useable = useable;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getZfbImgCode() {
		return zfbImgCode;
	}

	public void setZfbImgCode(String zfbImgCode) {
		this.zfbImgCode = zfbImgCode;
	}

	public Integer getScanAccount() {
		return scanAccount;
	}

	public void setScanAccount(Integer scanAccount) {
		this.scanAccount = scanAccount;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getVpnPassword() {
		return vpnPassword;
	}

	public void setVpnPassword(String vpnPassword) {
		this.vpnPassword = vpnPassword;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getBankType() {
		return bankType;
	}

	public void setBankType(Integer bankType) {
		this.bankType = bankType;
	}

	public String getUsb() {
		return usb;
	}

	public void setUsb(String usb) {
		this.usb = usb;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Double getBankAmount() {
		return bankAmount;
	}

	public void setBankAmount(Double bankAmount) {
		this.bankAmount = bankAmount;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Integer getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getTransferSwitch() {
		return transferSwitch;
	}

	public void setTransferSwitch(Integer transferSwitch) {
		this.transferSwitch = transferSwitch;
	}

	public Integer getSameBankSwitch() {
		return sameBankSwitch;
	}

	public void setSameBankSwitch(Integer sameBankSwitch) {
		this.sameBankSwitch = sameBankSwitch;
	}

	public String getSameBank() {
		return sameBank;
	}

	public void setSameBank(String sameBank) {
		this.sameBank = sameBank;
	}

	public String getCrossBank() {
		return crossBank;
	}

	public void setCrossBank(String crossBank) {
		this.crossBank = crossBank;
	}

	public Double getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(Double transferMoney) {
		this.transferMoney = transferMoney;
	}

	public Integer getAutoPay() {
		return autoPay;
	}

	public void setAutoPay(Integer autoPay) {
		this.autoPay = autoPay;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public double getDepositMin() {
		return depositMin;
	}

	public void setDepositMin(double depositMin) {
		this.depositMin = depositMin;
	}

	public double getDepositMax() {
		return depositMax;
	}

	public void setDepositMax(double depositMax) {
		this.depositMax = depositMax;
	}

}