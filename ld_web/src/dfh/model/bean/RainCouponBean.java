package dfh.model.bean;

public class RainCouponBean {
	private Integer code;
	private Double depositAmount;
	private Integer couponNum;
	private String coupon;
	private String msg;
	private String lastTime ;
	private Integer times ;

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public Integer getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	

}
