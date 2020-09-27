package dfh.action.vo;

/**
 * 自助优惠
 * @author Administrator
 *
 */
public class AutoYouHuiVo {
	
	private Double giftMoney ; //红利
	
	private Integer waterTimes ;//流水倍数需求
	
	private String message ;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(Double giftMoney) {
		this.giftMoney = giftMoney;
	}

	public Integer getWaterTimes() {
		return waterTimes;
	}

	public void setWaterTimes(Integer waterTimes) {
		this.waterTimes = waterTimes;
	}
	
	

}
