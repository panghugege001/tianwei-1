package dfh.remote.bean;

public class DepositPendingResponseBean {
	private String id;
	private String agcode;
	private String status;
	private String refno;
	private String paymentid;
	private String errdesc;

	public DepositPendingResponseBean(String id, String agcode, String status, String refno, String paymentid, String errdesc) {
		super();
		this.id = id;
		this.agcode = agcode;
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

	public String getAgcode() {
		return agcode;
	}

	public void setAgcode(String agcode) {
		this.agcode = agcode;
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
		return "DepositPendingResponseBean [agcode=" + agcode + ", errdesc=" + errdesc + ", id=" + id + ", paymentid=" + paymentid + ", refno=" + refno + ", status=" + status + "]";
	}

}
