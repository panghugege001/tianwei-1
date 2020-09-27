package app.model.vo;


public class WeekSentVO {

	private String pno;                //提案号
	private String promo;              //赠送金额
	private String tempCreateTime;
	private String remark;             //备注
	private String status;             //状态（0：未领取   1：已领取  2：未领取）
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getPromo() {
		return promo;
	}
	public void setPromo(String promo) {
		this.promo = promo;
	}
	public String getTempCreateTime() {
		return tempCreateTime;
	}
	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
