package dfh.model.bean;

import dfh.model.Users;

public class XimaDataVo {

	private String loginname;
	private Double total_bet;
	private Double total_win;
	private Users user;


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getTotal_bet() {
		return total_bet;
	}

	public void setTotal_bet(Double total_bet) {
		this.total_bet = total_bet;
	}

	public Double getTotal_win() {
		return total_win;
	}

	public void setTotal_win(Double total_win) {
		this.total_win = total_win;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
}
