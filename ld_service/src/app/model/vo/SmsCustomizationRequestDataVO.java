package app.model.vo;

public class SmsCustomizationRequestDataVO {

	private String loginName;//账号
	
	private String ip;//ip
	
	private String changePassword;//修改密码
	
	private String withdrawalAppliy;//提款申请
	
	private String depositExecut;//存款执行
	
	private String selfPromotion;//自助晋级礼金

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	public String getWithdrawalAppliy() {
		return withdrawalAppliy;
	}

	public void setWithdrawalAppliy(String withdrawalAppliy) {
		this.withdrawalAppliy = withdrawalAppliy;
	}

	public String getDepositExecut() {
		return depositExecut;
	}

	public void setDepositExecut(String depositExecut) {
		this.depositExecut = depositExecut;
	}

	public String getSelfPromotion() {
		return selfPromotion;
	}

	public void setSelfPromotion(String selfPromotion) {
		this.selfPromotion = selfPromotion;
	}
	
}
