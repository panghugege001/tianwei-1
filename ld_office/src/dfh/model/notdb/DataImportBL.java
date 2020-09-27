package dfh.model.notdb;

import java.math.BigInteger;
import java.util.Date;

public class DataImportBL {
	private String loginname;
	private String accountName;
	private String phone;
	private Integer level;
	private String agent;
	private String intro;
	private Date regTime;
	private Date lastLoginTime;
	private BigInteger day;
	private Integer loginTimes;
	private Double profitAllAmount;
	
	//比邻导入数据使用
	public DataImportBL(String loginname,String accountName,String phone,Integer level,String agent,String intro, Date regTime,Date lastLoginTime, BigInteger day, Integer loginTimes,Double profitAllAmount) {
		this.loginname = loginname;
		this.accountName = accountName;
		this.phone = phone;
		this.level = level;
		this.agent = agent;
		this.intro = intro;
		this.regTime = regTime;
		this.lastLoginTime = lastLoginTime;
		this.day = day;
		this.loginTimes = loginTimes;
		this.profitAllAmount = profitAllAmount;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public BigInteger getDay() {
		return day;
	}

	public void setDay(BigInteger day) {
		this.day = day;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Double getProfitAllAmount() {
		return profitAllAmount;
	}

	public void setProfitAllAmount(Double profitAllAmount) {
		this.profitAllAmount = profitAllAmount;
	}
}
