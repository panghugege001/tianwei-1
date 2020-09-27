package dfh.model.notdb;

import java.util.Date;

public class TransferRecords {
	private String billNo;
	private Boolean isTransferIn;
	private String passport;
	private Double amount;
	private Date billTime;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Boolean getIsTransferIn() {
		return isTransferIn;
	}

	public void setIsTransferIn(Boolean isTransferIn) {
		this.isTransferIn = isTransferIn;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	@Override
	public String toString() {
		return "TransferRecords [amount=" + amount + ", billNo=" + billNo + ", billTime=" + billTime + ", isTransferIn=" + isTransferIn + ", passport=" + passport + "]";
	}

}
