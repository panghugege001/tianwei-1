package app.model.vo;

public class SelfPromotionVO {
	
	// 玩家账号
	private String loginName;
	// 游戏平台
	private String platform;
	// 投注额
	private Double bet;
	
	public SelfPromotionVO() {

	}
	
	public SelfPromotionVO(String platform, Double bet) {
	
		this.platform = platform;
		this.bet = bet;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public Double getBet() {
		return bet;
	}
	
	public void setBet(Double bet) {
		this.bet = bet;
	}
	
}