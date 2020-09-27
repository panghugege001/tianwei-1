package dfh.model.bean;

public class Bean4Xima {

	private String userName;
	private Double betAmount;
	private Double profit;
	private String platfrom ;
	
	
	public Bean4Xima(){
		
	}
	
	
	public Bean4Xima(String userName, Double betAmount, Double profit){
		this.userName = userName;
		this.betAmount = betAmount;
		this.profit = profit;
	}
	
	public Bean4Xima(String userName, Double betAmount, Double profit,String platfrom){
		this.userName = userName;
		this.betAmount = betAmount;
		this.profit = profit;
		this.platfrom = platfrom;
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


	public String getPlatfrom() {
		return platfrom;
	}


	public void setPlatfrom(String platfrom) {
		this.platfrom = platfrom;
	}
	
	
}
