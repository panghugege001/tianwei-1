package app.model.vo;

public class FriendBonusVO {
	
	// 推荐码
	private Integer id;
	// 推荐奖金账户余额
	private Double money;
	// 玩家账号
	private String loginName;
	// 转账平台
	private String platform;
	// 转账金额
	private Double remit;
			
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
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

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}
	
}