package dfh.action.vo;

import dfh.model.Users;

public class KgVo {
	private String loginname ; 
	
	private Double bet ; 
	
	private Double profit ;
	
	private Users user ; 

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	} 
	
	

}
