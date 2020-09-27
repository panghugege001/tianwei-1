package dfh.action.vo;

import java.io.Serializable;

public class AgentReferralsVO implements Serializable {

	public AgentReferralsVO() {
		// TODO Auto-generated constructor stub
	}
	public AgentReferralsVO(String loginname, Double money) {
		super();
		this.loginname = loginname;
		this.money = money;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -2592252906084278627L;
	private String loginname;
	private Double money=0d;
	private Double bet = 0.00;
	private int personCount;
	private Double amount=0d;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money += money;
	}
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount += amount;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	

}
