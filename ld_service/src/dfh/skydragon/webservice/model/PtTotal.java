package dfh.skydragon.webservice.model;

public class PtTotal implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5775071301549011479L;
	private String loginname;
	private Double totalProfit;
	private Double totalBet;
	private Double winPointAll;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	public Double getWinPointAll() {
		return winPointAll;
	}
	public void setWinPointAll(Double winPointAll) {
		this.winPointAll = winPointAll;
	}
	public Double getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}
	public Double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(Double totalBet) {
		this.totalBet = totalBet;
	}
}
