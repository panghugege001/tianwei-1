package dfh.model.bean;

/**
 * 平台投注额
 *
 */
public class Bet {

	private String platform;
	private Double bet;
	
	public Bet(){}
	
	public Bet(String platform, Double bet){
		this.platform = platform;
		this.bet = bet;
	}
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
}
