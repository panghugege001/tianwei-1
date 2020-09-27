package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Accountinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "accountinfo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Accountinfo implements java.io.Serializable {

	// Fields

	private String loginname;
	private String accountName;
	private String accountNo;
	private String bank;
	private String accountType;
	private String accountCity;
	private String bankAddress;
	private Timestamp createtime;
	private Timestamp lastModifyTime;

	// Constructors

	/** default constructor */
	public Accountinfo() {
	}

	/** minimal constructor */
	public Accountinfo(String loginname, String accountName, String accountNo, String bank, String accountType, String accountCity, String bankAddress, Timestamp createtime) {
		this.loginname = loginname;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.bank = bank;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.createtime = createtime;
	}

	/** full constructor */
	public Accountinfo(String loginname, String accountName, String accountNo, String bank, String accountType, String accountCity, String bankAddress, Timestamp createtime, Timestamp lastModifyTime) {
		this.loginname = loginname;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.bank = bank;
		this.accountType = accountType;
		this.accountCity = accountCity;
		this.bankAddress = bankAddress;
		this.createtime = createtime;
		this.lastModifyTime = lastModifyTime;
	}

	// Property accessors
	@Id
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

	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "lastModifyTime")
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}