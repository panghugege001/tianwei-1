package dfh.model.bean;

public class AgSlotXima {

	private String userName;
	private Double validbetAmount;  //有效投注
	private Double netamount;  // 派彩
	
	public AgSlotXima(String userName, Double validbetAmount, Double netamount){
		this.userName = userName;
		this.validbetAmount=validbetAmount;
		this.netamount=netamount;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getNetamount() {
		return netamount;
	}

	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}

	public Double getValidbetAmount() {
		return validbetAmount;
	}

	public void setValidbetAmount(Double validbetAmount) {
		this.validbetAmount = validbetAmount;
	}
	
	
}
