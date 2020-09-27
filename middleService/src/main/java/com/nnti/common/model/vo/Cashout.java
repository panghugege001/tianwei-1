package com.nnti.common.model.vo;

public class Cashout implements java.io.Serializable {

	// Fields

	private String pno;
	private String title;
	private String loginname;
	private Double money;
	private String accountName;
	private String accountType;
	private String accountCity;
	private String bankAddress;
	private String accountNo;
	private String bank;
	private String ip;
	private String remark;
	private String notifyNote;
	private String notifyPhone;

	/** default constructor */
	public Cashout() {
	}

	/** minimal constructor */
	public Cashout(String pno, String title, String loginname, Double money, String accountName, String accountType, String accountCity, String bankAddress, String accountNo, String bank) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.money = money;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.accountNo = accountNo;
		this.bank = bank;
	}

	/** full constructor */
	public Cashout(String pno, String title, String loginname, Double money, String accountName, String accountType, String accountCity, String bankAddress, String accountNo, String bank,
			String ip, String remark, String notifyNote, String notifyPhone) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.money = money;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.accountNo = accountNo;
		this.bank = bank;
		this.ip = ip;
		this.remark = remark;
		this.notifyNote = notifyNote;
		this.notifyPhone = notifyPhone;
	}

	public String getPno() {
		return this.pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountCity() {
		return this.accountCity;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public String getBankAddress() {
		return this.bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}


	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNotifyNote() {
		return this.notifyNote;
	}

	public void setNotifyNote(String notifyNote) {
		this.notifyNote = notifyNote;
	}

	public String getNotifyPhone() {
		return this.notifyPhone;
	}

	public void setNotifyPhone(String notifyPhone) {
		this.notifyPhone = notifyPhone;
	}

}