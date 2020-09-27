package dfh.remote.bean;

public class WithdrawalConfirmationResponseBean {
	private String id;
	private String status;
	private String refno;
	private String paymentid;
	private String errdesc;

	public WithdrawalConfirmationResponseBean(String id, String status, String refno, String paymentid, String errdesc) {
		super();
		this.id = id;
		this.status = status;
		this.refno = refno;
		this.paymentid = paymentid;
		this.errdesc = errdesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	public String getErrdesc() {
		return errdesc;
	}

	public void setErrdesc(String errdesc) {
		this.errdesc = errdesc;
	}

	@Override
	public String toString() {
		return "WithdrawalConfirmationResponseBean [errdesc=" + errdesc + ", id=" + id + ", paymentid=" + paymentid + ", refno=" + refno + ", status=" + status + "]";
	}

}
