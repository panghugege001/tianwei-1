package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Xima entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "xima", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Xima implements java.io.Serializable {

	// Fields

	private String pno;
	private String title;
	private String loginname;
	private String payType;
	private Double firstCash;
	private Double tryCredit;
	private Timestamp startTime;
	private Timestamp endTime;
	private Double rate;
	private String remark;

	// Constructors

	/** default constructor */
	public Xima() {
	}

	/** minimal constructor */
	public Xima(String pno, String title, String loginname, Double firstCash, Double tryCredit, Double rate) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.firstCash = firstCash;
		this.tryCredit = tryCredit;
		this.rate = rate;
	}

	/** full constructor */
	public Xima(String pno, String title, String loginname, String payType, Double firstCash, Double tryCredit, Timestamp startTime, Timestamp endTime, Double rate, String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.payType = payType;
		this.firstCash = firstCash;
		this.tryCredit = tryCredit;
		this.startTime = startTime;
		this.endTime = endTime;
		this.rate = rate;
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

	@javax.persistence.Column(name = "payType")
	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@javax.persistence.Column(name = "firstCash")
	public Double getFirstCash() {
		return this.firstCash;
	}

	public void setFirstCash(Double firstCash) {
		this.firstCash = firstCash;
	}

	@javax.persistence.Column(name = "tryCredit")
	public Double getTryCredit() {
		return this.tryCredit;
	}

	public void setTryCredit(Double tryCredit) {
		this.tryCredit = tryCredit;
	}

	@javax.persistence.Column(name = "startTime")
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@javax.persistence.Column(name = "endTime")
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@javax.persistence.Column(name = "rate")
	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}