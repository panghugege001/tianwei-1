package app.model.vo;

public class SlotMachineBigBangVO {
	
	// 编号
	private Integer id;
	// 游戏平台
	private String platform;
	// 输赢金额
	private Double netWinOrLose;
	// 红利
	private Double bonus;
	// 礼金
	private Double giftMoney;
	// 派发时间
	private String distributeTime;
	// 状态
	private String status;
	// 玩家账号
	private String loginName;
	// IP地址
	private String ip;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Double getNetWinOrLose() {
		return netWinOrLose;
	}

	public void setNetWinOrLose(Double netWinOrLose) {
		this.netWinOrLose = netWinOrLose;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(Double giftMoney) {
		this.giftMoney = giftMoney;
	}

	public String getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(String distributeTime) {
		this.distributeTime = distributeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}