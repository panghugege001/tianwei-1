package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cashin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "cashin", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Cashin implements java.io.Serializable {

	// Fields

	private String pno;
	private String title;
	private String loginname;
	private String aliasName;
	private Double money;
	private String accountNo;
	private String corpBankName;
	private String remark;
	private Date cashintime;
	private Double fee;
	// Constructors

	/** default constructor */
	public Cashin() {
	}

	/** minimal constructor */
	public Cashin(String pno, String title, String loginname, Double money) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.money = money;
	}

	/** full constructor */
	public Cashin(String pno, String title, String loginname, String aliasName, Double money, String accountNo, String corpBankName, String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.aliasName = aliasName;
		this.money = money;
		this.accountNo = accountNo;
		this.corpBankName = corpBankName;
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

	@javax.persistence.Column(name = "aliasName")
	public String getAliasName() {
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	@javax.persistence.Column(name = "money")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@javax.persistence.Column(name = "accountNo")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@javax.persistence.Column(name = "corpBankName")
	public String getCorpBankName() {
		return this.corpBankName;
	}

	public void setCorpBankName(String corpBankName) {
		this.corpBankName = corpBankName;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "cashintime")
	public Date getCashintime() {
		return cashintime;
	}

	public void setCashintime(Date cashintime) {
		this.cashintime = cashintime;
	}
	
	@javax.persistence.Column(name = "fee")
	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	
	
}