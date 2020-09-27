package dfh.skydragon.webservice.model;

public class WinPointInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8802131144474381900L;
	private String loginname;
	private Double profitTotal;
	private Double betTotal;
	private Double winPointTotal;
	private Integer winOrderBy;
	public WinPointInfo() {
	}
	public WinPointInfo(String loginname, Double profitTotal, Double betTotal,
			Double winPointTotal,Integer winOrderBy) {
		super();
		this.loginname = loginname;
		this.profitTotal = profitTotal;
		this.betTotal = betTotal;
		this.winPointTotal = winPointTotal;
		this.winOrderBy = winOrderBy;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getProfitTotal() {
		return profitTotal;
	}
	public void setProfitTotal(Double profitTotal) {
		this.profitTotal = profitTotal;
	}
	public Double getBetTotal() {
		return betTotal;
	}
	public void setBetTotal(Double betTotal) {
		this.betTotal = betTotal;
	}
	public Double getWinPointTotal() {
		return winPointTotal;
	}
	public void setWinPointTotal(Double winPointTotal) {
		this.winPointTotal = winPointTotal;
	}
	public Integer getWinOrderBy() {
		return winOrderBy;
	}
	public void setWinOrderBy(Integer winOrderBy) {
		this.winOrderBy = winOrderBy;
	}
	
}
