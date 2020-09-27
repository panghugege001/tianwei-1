package app.model.vo;

public class EmigratedBonusVO {

	// 闯龙门奖金余额
	private Double money;
	// 玩家账号
	private String loginName;
	// 转账金额
	private Double remit;
	// 目标账户
	private String platform;
	// 闯龙门报名等级
	private String grade;
	
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

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
}