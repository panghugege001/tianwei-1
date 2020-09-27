package dfh.model;

import java.util.Date;

public class ProxyFlow {

	public String agent;// 代理账户
	private Date createtime;// 注册时间
	private String referWebsite;// 代理网址
	public Integer agentCount;// 会员注册量
	public Integer agentActiveCount;// 活跃会员数量
	private String registerDeposit;// 开户存款转化率
	private String registerDepositAll;// 总开户存款转化率
	private String amountProfit;// 赢利额
	private String amountProfitAll;//总赢利额
	private String bettotal;// 投注额
	private String bettotalAll;// 总投注额
	private String commission;// 佣金
	private String commissionAll;//总佣金
	private Integer ipAccess;// IP量
	private Integer ipAccessAll;
	private Integer pvAccess;// PV量
	private Integer pvAccessAll;
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getReferWebsite() {
		return referWebsite;
	}
	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}
	public Integer getAgentCount() {
		return agentCount;
	}
	public void setAgentCount(Integer agentCount) {
		this.agentCount = agentCount;
	}
	public Integer getAgentActiveCount() {
		return agentActiveCount;
	}
	public void setAgentActiveCount(Integer agentActiveCount) {
		this.agentActiveCount = agentActiveCount;
	}
	
	public String getRegisterDeposit() {
		return registerDeposit;
	}
	public void setRegisterDeposit(String registerDeposit) {
		this.registerDeposit = registerDeposit;
	}
	public String getAmountProfit() {
		return amountProfit;
	}
	public void setAmountProfit(String amountProfit) {
		this.amountProfit = amountProfit;
	}
	public String getBettotal() {
		return bettotal;
	}
	public void setBettotal(String bettotal) {
		this.bettotal = bettotal;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public Integer getIpAccess() {
		return ipAccess;
	}
	public void setIpAccess(Integer ipAccess) {
		this.ipAccess = ipAccess;
	}
	public Integer getPvAccess() {
		return pvAccess;
	}
	public void setPvAccess(Integer pvAccess) {
		this.pvAccess = pvAccess;
	}
	
	public String getAmountProfitAll() {
		return amountProfitAll;
	}
	public void setAmountProfitAll(String amountProfitAll) {
		this.amountProfitAll = amountProfitAll;
	}
	public String getCommissionAll() {
		return commissionAll;
	}
	public void setCommissionAll(String commissionAll) {
		this.commissionAll = commissionAll;
	}
	
	public String getBettotalAll() {
		return bettotalAll;
	}
	public void setBettotalAll(String bettotalAll) {
		this.bettotalAll = bettotalAll;
	}
	public Integer getIpAccessAll() {
		return ipAccessAll;
	}
	public void setIpAccessAll(Integer ipAccessAll) {
		this.ipAccessAll = ipAccessAll;
	}
	public Integer getPvAccessAll() {
		return pvAccessAll;
	}
	public void setPvAccessAll(Integer pvAccessAll) {
		this.pvAccessAll = pvAccessAll;
	}
	public String getRegisterDepositAll() {
		return registerDepositAll;
	}
	public void setRegisterDepositAll(String registerDepositAll) {
		this.registerDepositAll = registerDepositAll;
	}
	public String toString() {
		return "Test [agent=" + agent + ", referWebsite=" + referWebsite
				+ ", agentCount=" + agentCount + ", agentActiveCount="
				+ agentActiveCount + ", registerDeposit=" + registerDeposit
				+ ", amountProfit=" + amountProfit + ", bettotal=" + bettotal
				+ ", commission=" + commission + ", ipAccess=" + ipAccess
				+ ", commissionAll=" + commissionAll + ", amountProfitAll=" + amountProfitAll
				+ ", pvAccess=" + pvAccess + "]";
	}

}
