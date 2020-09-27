package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cashout entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "cashout", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
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
	private String phone;
	private String email;
	private String ip;
	private String remark;
	private String notifyNote;
	private String notifyPhone;

	// Constructors

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
			String phone, String email, String ip, String remark, String notifyNote, String notifyPhone) {
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
		this.phone = phone;
		this.email = email;
		this.ip = ip;
		this.remark = remark;
		this.notifyNote = notifyNote;
		this.notifyPhone = notifyPhone;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "pno")
	public String getPno() {
		return this.pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "money")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@javax.persistence.Column(name = "accountName")
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@javax.persistence.Column(name = "accountType")
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@javax.persistence.Column(name = "accountCity")
	public String getAccountCity() {
		return this.accountCity;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	@javax.persistence.Column(name = "bankAddress")
	public String getBankAddress() {
		return this.bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	@javax.persistence.Column(name = "accountNo")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@javax.persistence.Column(name = "bank")
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@javax.persistence.Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@javax.persistence.Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@javax.persistence.Column(name = "ip")
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "notifyNote")
	public String getNotifyNote() {
		return this.notifyNote;
	}

	public void setNotifyNote(String notifyNote) {
		this.notifyNote = notifyNote;
	}

	@javax.persistence.Column(name = "notifyPhone")
	public String getNotifyPhone() {
		return this.notifyPhone;
	}

	public void setNotifyPhone(String notifyPhone) {
		this.notifyPhone = notifyPhone;
	}

}