package dfh.model.notdb;

/**
 * 总报表
 * 
 * @author Administrator
 * 
 */
public class Report {
	private Double sumBillAmount;
	private Double sumFactAmount;
	private Double sumResult;
	private Integer sumAttend;
	private String winPercent;
	private String loginname;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getSumFactAmount() {
		return sumFactAmount;
	}

	public void setSumFactAmount(Double sumFactAmount) {
		this.sumFactAmount = sumFactAmount;
	}

	public Double getSumBillAmount() {
		return sumBillAmount;
	}

	public void setSumBillAmount(Double sumBillAmount) {
		this.sumBillAmount = sumBillAmount;
	}

	public Double getSumResult() {
		return sumResult;
	}

	public void setSumResult(Double sumResult) {
		this.sumResult = sumResult;
	}

	public Integer getSumAttend() {
		return sumAttend;
	}

	public void setSumAttend(Integer sumAttend) {
		this.sumAttend = sumAttend;
	}

	public String getWinPercent() {
		return winPercent;
	}

	public void setWinPercent(String winPercent) {
		this.winPercent = winPercent;
	}

	@Override
	public String toString() {
		return "Report [loginname=" + loginname + ", sumAttend=" + sumAttend + ", sumBillAmount=" + sumBillAmount + ", sumResult=" + sumResult + ", winPercent=" + winPercent + "]";
	}

}
