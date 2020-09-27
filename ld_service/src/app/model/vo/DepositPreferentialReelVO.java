package app.model.vo;

public class DepositPreferentialReelVO {
	
	// 玩家账号
	private String loginName;
	// 转账平台
	private String type;
	// 优惠代码
	private String code;
	// 转账金额
	private Double remit;
	// 操作IP
	private String ip;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}