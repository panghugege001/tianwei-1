package app.model.vo;

public class LosePromoVO extends BaseVO {

	// 玩家账号
	private String loginName;
	// 提案号
	private String pno;
	// 提案状态
	private Integer proposalFlag;
	// 操作IP地址
	private String ip;
	// 平台编码
	private String platform;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public Integer getProposalFlag() {
		return proposalFlag;
	}

	public void setProposalFlag(Integer proposalFlag) {
		this.proposalFlag = proposalFlag;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
}