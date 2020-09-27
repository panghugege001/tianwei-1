package dfh.model.notdb;

import java.math.BigInteger;
import java.util.Date;

public class AgentAnalysis {
	private String loginname ;
	private Date createtime ;
	private String partner ;
	private Integer warnflag ;
	private Integer agentType ;
	private BigInteger regnum;
	private BigInteger depnum ;
	private Double betall ;
	private Double depositall ;
	private Double drawall ;
	private Double slotaccount ;
	private Double credit ;
	private Date lastLoginTime;
	private Integer loginTimes;
	private BigInteger day;
	private Double profitall ; //存提款的输赢
	private Double agentprofitall ; //游戏输赢
	private Double agentdrwalall ; //代理提款总金额
	private Integer agentActiveCount;//活跃会员数量
	private Double agentFriendBonus;//代理下线好友推荐金
	private Integer isValid;//有效代理0-无效 1-有效
	
	
	public Integer getAgentActiveCount() {
		return agentActiveCount;
	}
	public void setAgentActiveCount(Integer agentActiveCount) {
		this.agentActiveCount = agentActiveCount;
	}

	
	
	public AgentAnalysis() {
		super();
	}
	public AgentAnalysis(String loginname, Date createtime, String partner, Integer warnflag, Integer agentType,
			BigInteger regnum, BigInteger depnum, Double betall, Double depositall, Double drawall, Double slotaccount,
			Double credit, Date lastLoginTime, Integer loginTimes, BigInteger day, Double profitall,
			Double agentprofitall,Double agentdrwalall,Integer agentActiveCount) {
		super();
		this.loginname = loginname;
		this.createtime = createtime;
		this.partner = partner;
		this.warnflag = warnflag;
		this.agentType = agentType;
		this.regnum = regnum;
		this.depnum = depnum;
		this.betall = betall;
		this.depositall = depositall;
		this.drawall = drawall;
		this.slotaccount = slotaccount;
		this.credit = credit;
		this.lastLoginTime = lastLoginTime;
		this.loginTimes = loginTimes;
		this.day = day;
		this.profitall = profitall;
		this.agentprofitall = agentprofitall;
		this.agentdrwalall = agentdrwalall;
		this.agentActiveCount = agentActiveCount;
	}
	public AgentAnalysis(String loginname, Date createtime, String partner, Integer warnflag, Integer agentType,
			BigInteger regnum, BigInteger depnum, Double betall, Double depositall, Double drawall, Double slotaccount,
			Double credit, Date lastLoginTime, Integer loginTimes, BigInteger day, Double profitall,
			Double agentprofitall,Double agentdrwalall,Integer agentActiveCount, Double agentFriendBonus) {
		super();
		this.loginname = loginname;
		this.createtime = createtime;
		this.partner = partner;
		this.warnflag = warnflag;
		this.agentType = agentType;
		this.regnum = regnum;
		this.depnum = depnum;
		this.betall = betall;
		this.depositall = depositall;
		this.drawall = drawall;
		this.slotaccount = slotaccount;
		this.credit = credit;
		this.lastLoginTime = lastLoginTime;
		this.loginTimes = loginTimes;
		this.day = day;
		this.profitall = profitall;
		this.agentprofitall = agentprofitall;
		this.agentdrwalall = agentdrwalall;
		this.agentActiveCount = agentActiveCount;
		this.agentFriendBonus = agentFriendBonus;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public Integer getWarnflag() {
		return warnflag;
	}
	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	public Integer getAgentType() {
		return agentType;
	}
	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}
	public BigInteger getRegnum() {
		return regnum;
	}
	public void setRegnum(BigInteger regnum) {
		this.regnum = regnum;
	}
	public BigInteger getDepnum() {
		return depnum;
	}
	public void setDepnum(BigInteger depnum) {
		this.depnum = depnum;
	}
	public Double getBetall() {
		return betall;
	}
	public void setBetall(Double betall) {
		this.betall = betall;
	}
	public Double getDepositall() {
		return depositall;
	}
	public void setDepositall(Double depositall) {
		this.depositall = depositall;
	}
	public Double getDrawall() {
		return drawall;
	}
	public void setDrawall(Double drawall) {
		this.drawall = drawall;
	}
	public Double getSlotaccount() {
		return slotaccount;
	}
	public void setSlotaccount(Double slotaccount) {
		this.slotaccount = slotaccount;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Integer getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}
	public BigInteger getDay() {
		return day;
	}
	public void setDay(BigInteger day) {
		this.day = day;
	}
	public Double getProfitall() {
		return profitall;
	}
	public void setProfitall(Double profitall) {
		this.profitall = profitall;
	}
	public Double getAgentprofitall() {
		return agentprofitall;
	}
	public void setAgentprofitall(Double agentprofitall) {
		this.agentprofitall = agentprofitall;
	}
	public Double getAgentdrwalall() {
		return agentdrwalall;
	}
	public void setAgentdrwalall(Double agentdrwalall) {
		this.agentdrwalall = agentdrwalall;
	}
	public Double getAgentFriendBonus() {
		return agentFriendBonus;
	}
	public void setAgentFriendBonus(Double agentFriendBonus) {
		this.agentFriendBonus = agentFriendBonus;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	
	

}
