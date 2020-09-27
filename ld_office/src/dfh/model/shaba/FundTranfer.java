package dfh.model.shaba;

import java.math.BigDecimal;

/**
 * 资金转账类
 * @author Jalen
 *
 */
public class FundTranfer implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String trans_id;
	private String transfer_date;
	private BigDecimal amount;
	private BigDecimal before_amount;
	private BigDecimal after_amount;
	private Integer currency;
	private Integer status; //0:ok 1:Failed 2:Pending
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getTransfer_date() {
		return transfer_date;
	}
	public void setTransfer_date(String transfer_date) {
		this.transfer_date = transfer_date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBefore_amount() {
		return before_amount;
	}
	public void setBefore_amount(BigDecimal before_amount2) {
		this.before_amount = before_amount2;
	}
	public BigDecimal getAfter_amount() {
		return after_amount;
	}
	public void setAfter_amount(BigDecimal after_amount) {
		this.after_amount = after_amount;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
