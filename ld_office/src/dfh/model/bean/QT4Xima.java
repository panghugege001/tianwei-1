package dfh.model.bean;

public class QT4Xima {

	private String userName;
	private Double betAmount;
	private Double profit;
	
	public QT4Xima(String userName, Double betAmount, Double profit){
		this.userName = userName;
		this.betAmount = betAmount;
		this.profit = profit;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	
	
}
