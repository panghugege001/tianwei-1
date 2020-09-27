package dfh.model.notdb;

import java.util.Date;

public class BalanceRecords {
	private String transNo;
	private Date transTime;
	private String transType;
	private String passport;
	private double srcAmount;
	private double transAmount;
	private double desAmount;

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public double getSrcAmount() {
		return srcAmount;
	}

	public void setSrcAmount(double srcAmount) {
		this.srcAmount = srcAmount;
	}

	public double getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(double transAmount) {
		this.transAmount = transAmount;
	}

	public double getDesAmount() {
		return desAmount;
	}

	public void setDesAmount(double desAmount) {
		this.desAmount = desAmount;
	}

	@Override
	public String toString() {
		return "BalanceRecords [desAmount=" + desAmount + ", passport=" + passport + ", srcAmount=" + srcAmount + ", transAmount=" + transAmount + ", transNo=" + transNo + ", transTime=" + transTime
				+ ", transType=" + transType + "]";
	}

}
