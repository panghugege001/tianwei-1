package app.model.vo;

public class UnderLineProposalVO {

	//
	private String loginName;
	//
	private String tempCreateTime;
	//
	private Double money;
	//
	private Double giftAmount;
	//
	private String typeName;
	
	public UnderLineProposalVO() {
		
	}
	
	public UnderLineProposalVO(String loginName, String tempCreateTime, Double money, Double giftAmount, String typeName) {
	
		this.loginName = loginName;
		this.tempCreateTime = tempCreateTime;
		this.money = money;
		this.giftAmount = giftAmount;
		this.typeName = typeName;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(Double giftAmount) {
		this.giftAmount = giftAmount;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}	
}