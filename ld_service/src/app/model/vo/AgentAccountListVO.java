package app.model.vo;

public class AgentAccountListVO extends BaseVO {
	
	// 玩家账号
	private String loginName;
	// 起始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 会员帐号
	private String account;
	// 取款类型
	private Integer proposalType;
	// 游戏平台
	private String platform;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public Integer getProposalType() {
		return proposalType;
	}
	
	public void setProposalType(Integer proposalType) {
		this.proposalType = proposalType;
	}

	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}