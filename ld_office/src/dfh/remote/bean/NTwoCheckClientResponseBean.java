package dfh.remote.bean;

import java.math.BigDecimal;

import dfh.remote.ErrorCode;

public class NTwoCheckClientResponseBean {
	
	private String id;
	private String userid;
	private BigDecimal balance;
	private String currencyid;
	private String status;
	private String errdesc;
	
	public NTwoCheckClientResponseBean() {
		
	}
	
	public NTwoCheckClientResponseBean(String id, String userid, BigDecimal balance, String currencyid, String status,
			String errdesc) {
		super();
		this.id = id;
		this.userid = userid;
		this.balance = balance;
		this.currencyid = currencyid;
		this.status = status;
		this.errdesc = errdesc;
	}

	public boolean isSuccess() {
		return ErrorCode.SUCCESS.getCode().equals(status);
	}
	
	public boolean isFail() {
		return !isSuccess();
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCurrencyid() {
		return currencyid;
	}
	public void setCurrencyid(String currencyid) {
		this.currencyid = currencyid;
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

	@Override
	public String toString() {
		return "NTwoCheckClientResponseBean [id=" + id + ", userid=" + userid + ", balance=" + balance + ", currencyid="
				+ currencyid + ", status=" + status + ", errdesc=" + errdesc + "]";
	}
}
