package dfh.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Userstatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "userstatus", catalog = "tianwei")
public class Userstatus implements java.io.Serializable {

	// Fields

	private String loginname;
	private Integer loginerrornum;
	private Integer mailflag;
	private Integer cashinwrong;
	private Integer touzhuflag=0;
	private Integer times=0;
	private Timestamp starttime;
	private Double firstCash=0.0;
	private Double amount=0.0;
	private Double slotaccount;
	// Constructors

	/** default constructor */
	public Userstatus() {
	}

	/** minimal constructor */
	public Userstatus(String loginname) {
		this.loginname = loginname;
	}

	/** full constructor */
	public Userstatus(String loginname, Integer loginerrornum, Integer mailflag) {
		this.loginname = loginname;
		this.loginerrornum = loginerrornum;
		this.mailflag = mailflag;
	}

	// Property accessors
	@Id
	@Column(name = "loginname", unique = true, nullable = false, length = 30)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "loginerrornum")
	public Integer getLoginerrornum() {
		return this.loginerrornum;
	}

	public void setLoginerrornum(Integer loginerrornum) {
		this.loginerrornum = loginerrornum;
	}

	@Column(name = "mailflag")
	public Integer getMailflag() {
		return this.mailflag;
	}

	public void setMailflag(Integer mailflag) {
		this.mailflag = mailflag;
	}

	@Column(name = "cashinwrong")
	public Integer getCashinwrong() {
		return cashinwrong;
	}

	public void setCashinwrong(Integer cashinwrong) {
		this.cashinwrong = cashinwrong;
	}
	
	@Column(name = "touzhuflag")
	public Integer getTouzhuflag() {
		return touzhuflag;
	}

	public void setTouzhuflag(Integer touzhuflag) {
		this.touzhuflag = touzhuflag;
	}

	@Column(name = "times")
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	@Column(name = "starttime")
	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	@Column(name = "firstCash")
	public Double getFirstCash() {
		return firstCash;
	}

	public void setFirstCash(Double firstCash) {
		this.firstCash = firstCash;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@Column(name = "slotaccount")
	public Double getSlotaccount() {
		return slotaccount;
	}

	public void setSlotaccount(Double slotaccount) {
		this.slotaccount = slotaccount;
	}

}