package dfh.action.vo;

import java.io.Serializable;

public class FundStatisticsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415997706233463852L;
	private Integer totalCount;
	private Double amount;
	private String bankName;
	public FundStatisticsVO(Integer totalCount, Double amount, String bankName) {
		super();
		this.totalCount = totalCount;
		this.amount = amount;
		this.bankName = bankName;
	}
	public FundStatisticsVO(Integer totalCount, Double amount) {
		super();
		this.totalCount = totalCount;
		this.amount = amount;
	}
	
	
	
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	

}
