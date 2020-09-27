package dfh.model.notdb;

import java.math.BigInteger;
import java.util.Date;

public class CustomerAnalysis {
	private String loginname;
	private Integer warnflag;
	private Integer level;
	private String agent;
	private String intro;
	private Double depositAmount;
	private Double netpayAmount;
	private Double withdrawAmount;
	private Double profileAmount;
	private Date regTime;
	private Date lastLoginTime;
	private BigInteger day;
	private Integer loginTimes;
	private Double profitAllAmount;
	private Double friendBonus;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
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
	public Double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public Double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public Double getProfileAmount() {
		return profileAmount;
	}
	public void setProfileAmount(Double profileAmount) {
		this.profileAmount = profileAmount;
	}
	public CustomerAnalysis(String loginname,Integer warnflag,Integer level,String agent,String intro, Double depositAmount,Double netpayAmount,
			Double withdrawAmount, Double profileAmount, Date regTime,
			Date lastLoginTime, BigInteger day, Integer loginTimes,Double profitAllAmount) {
		this.loginname = loginname;
		this.warnflag = warnflag;
		this.level = level;
		this.agent = agent;
		this.intro = intro;
		this.netpayAmount = netpayAmount;
		this.depositAmount = depositAmount;
		this.withdrawAmount = withdrawAmount;
		this.profileAmount = profileAmount;
		this.regTime = regTime;
		this.lastLoginTime = lastLoginTime;
		this.day = day;
		this.loginTimes = loginTimes;
		this.profitAllAmount = profitAllAmount;
	}
	public CustomerAnalysis(String loginname,Integer warnflag,Integer level,String agent,String intro, Double depositAmount,Double netpayAmount,
			Double withdrawAmount, Double profileAmount, Date regTime,
			Date lastLoginTime, BigInteger day, Integer loginTimes,Double profitAllAmount, Double friendBonus) {
		this.loginname = loginname;
		this.warnflag = warnflag;
		this.level = level;
		this.agent = agent;
		this.intro = intro;
		this.netpayAmount = netpayAmount;
		this.depositAmount = depositAmount;
		this.withdrawAmount = withdrawAmount;
		this.profileAmount = profileAmount;
		this.regTime = regTime;
		this.lastLoginTime = lastLoginTime;
		this.day = day;
		this.loginTimes = loginTimes;
		this.profitAllAmount = profitAllAmount;
		this.friendBonus = friendBonus;
	}
	public Double getNetpayAmount() {
		return netpayAmount;
	}
	public void setNetpayAmount(Double netpayAmount) {
		this.netpayAmount = netpayAmount;
	}
	public Double getProfitAllAmount() {
		return profitAllAmount;
	}
	public void setProfitAllAmount(Double profitAllAmount) {
		this.profitAllAmount = profitAllAmount;
	}

	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
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
	public Integer getWarnflag() {
		return warnflag;
	}
	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	public Double getFriendBonus() {
		return friendBonus;
	}
	public void setFriendBonus(Double friendBonus) {
		this.friendBonus = friendBonus;
	}
	
}
