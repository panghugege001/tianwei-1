package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Offer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "offer", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Offer implements java.io.Serializable {

	// Fields

	private String pno;
	private String title;
	private String loginname;
	private Double firstCash;
	private Double money;
	private String remark;

	// Constructors

	/** default constructor */
	public Offer() {
	}

	/** minimal constructor */
	public Offer(String pno, String title, String loginname, Double firstCash, Double money) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.firstCash = firstCash;
		this.money = money;
	}

	/** full constructor */
	public Offer(String pno, String title, String loginname, Double firstCash, Double money, String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.firstCash = firstCash;
		this.money = money;
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

	@javax.persistence.Column(name = "firstCash")
	public Double getFirstCash() {
		return this.firstCash;
	}

	public void setFirstCash(Double firstCash) {
		this.firstCash = firstCash;
	}

	@javax.persistence.Column(name = "money")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}