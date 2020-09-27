package dfh.remote.bean;

public class GetTurnOverResponseBean {
	private String id;
	private String status;
	private String errdesc;
	private String currencyid;
	private Double turnover;
	public GetTurnOverResponseBean(String id, String status, String errdesc,
			String currencyid, Double turnover) {
		super();
		this.id = id;
		this.status = status;
		this.errdesc = errdesc;
		this.currencyid = currencyid;
		this.turnover = turnover;
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
	public String getErrdesc() {
		return errdesc;
	}
	public void setErrdesc(String errdesc) {
		this.errdesc = errdesc;
	}
	public String getCurrencyid() {
		return currencyid;
	}
	public void setCurrencyid(String currencyid) {
		this.currencyid = currencyid;
	}
	public Double getTurnover() {
		return turnover;
	}
	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}
}
