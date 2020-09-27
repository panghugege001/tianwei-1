package dfh.model.bean;

import java.util.Date;

public class AuditDetail {

	public AuditDetail() {
	}

	public AuditDetail(String loginname, String pno, Double lastTotalAmount, Double localCreditChange, Double currentTotalAmount,
			Double loseAmount) {
		super();
		this.loginname = loginname;
		this.pno = pno;
		this.lastTotalAmount = lastTotalAmount;
		this.localCreditChange = localCreditChange;
		this.currentTotalAmount = currentTotalAmount;
		this.loseAmount = loseAmount;
	}

	private String loginname;
	private String pno;
	private String output;
	private Double lastTotalAmount;
	private Double localCreditChange;
	private Double currentTotalAmount;
	private Double loseAmount;
	private Date lastTime;
	private Date currentTime;

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public Double getLastTotalAmount() {
		return lastTotalAmount;
	}

	public void setLastTotalAmount(Double lastTotalAmount) {
		this.lastTotalAmount = lastTotalAmount;
	}

	public Double getLocalCreditChange() {
		return localCreditChange;
	}

	public void setLocalCreditChange(Double localCreditChange) {
		this.localCreditChange = localCreditChange;
	}

	public Double getCurrentTotalAmount() {
		return currentTotalAmount;
	}

	public void setCurrentTotalAmount(Double currentTotalAmount) {
		this.currentTotalAmount = currentTotalAmount;
	}

	public Double getLoseAmount() {
		return loseAmount;
	}

	public void setLoseAmount(Double loseAmount) {
		this.loseAmount = loseAmount;
	}

}
