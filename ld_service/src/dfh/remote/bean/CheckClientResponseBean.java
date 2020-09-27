package dfh.remote.bean;

public class CheckClientResponseBean {
	private String id;
	private String userid;
	private Double balance;
	private String currencyid;
	private String location;

	public CheckClientResponseBean(String id, String userid, Double balance, String currencyid, String location) {
		super();
		this.id = id;
		this.userid = userid;
		this.balance = balance;
		this.currencyid = currencyid;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getCurrencyid() {
		return currencyid;
	}

	public void setCurrencyid(String currencyid) {
		this.currencyid = currencyid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "CheckClientResponseBean [balance=" + balance + ", currencyid=" + currencyid + ", id=" + id + ", location=" + location + ", userid=" + userid + "]";
	}

}
