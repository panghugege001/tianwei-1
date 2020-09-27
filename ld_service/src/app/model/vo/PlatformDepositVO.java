package app.model.vo;

public class PlatformDepositVO {

	// 存送优惠编号
	private Integer id;
	// 存送优惠平台编号
	private String platform;
	// 存送优惠类型编号
	private Integer youhuiType;
	// 玩家账号
	private String loginName;
	// 转账金额
	private Double remit;
	// 优惠红利
	private Double giftMoney;
	// 提示信息
	private String message;
	// 转账ID
	private String transferId;
	// 转账ID
	private String sid;
	// 备注，用于区分是官网优惠还是手机APP优惠
	private String remark;
	
	private String password;//用户密码，mg转账需要
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

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
	
	public Integer getYouhuiType() {
		return youhuiType;
	}

	public void setYouhuiType(Integer youhuiType) {
		this.youhuiType = youhuiType;
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

	public Double getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(Double giftMoney) {
		this.giftMoney = giftMoney;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTransferId() {
		return transferId;
	}
	
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	
}