package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Rebankinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "rebankinfo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Rebankinfo implements java.io.Serializable {

	// Fields

	private String pno;
	private String title;
	private String loginname;
	private String accountName;
	private String accountNo;
	private String bank;
	private String accountType;
	private String accountCity;
	private String bankAddress;
	private String ip;
	private String remark;

	// Constructors

	/** default constructor */
	public Rebankinfo() {
	}

	/** minimal constructor */
	public Rebankinfo(String pno, String title, String loginname, String accountName, String accountNo, String bank, String accountType, String accountCity, String bankAddress, String ip) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.bank = bank;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.ip = ip;
	}

	/** full constructor */
	public Rebankinfo(String pno, String title, String loginname, String accountName, String accountNo, String bank, String accountType, String accountCity, String bankAddress, String ip,
			String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.bank = bank;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.ip = ip;
		this.remark = remark;
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

	@javax.persistence.Column(name = "accountName")
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

}