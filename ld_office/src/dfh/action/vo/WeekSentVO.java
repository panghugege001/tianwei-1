package dfh.action.vo;

import java.io.Serializable;

import dfh.model.Users;

public class WeekSentVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Double startAmount = 10000.00;//开始起送的最小投注额
	public static final int times = 10;//十倍流水
	
	private String loginname;//帐号
	private Double sentAmount;//赠送金额
	private Double betAmount;//投注总额
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getSentAmount() {
		return sentAmount;
	}
	public void setSentAmount(Double sentAmount) {
		this.sentAmount = sentAmount;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	
	
	public WeekSentVO(String loginname, Double amount) {
		super();
		this.loginname = loginname;
		this.betAmount = amount;
		if(betAmount>=10000 && betAmount<30000){
			this.sentAmount = 8.00;
		}else if(betAmount>=30000 && betAmount<60000){
			this.sentAmount = 12.00;
		}else if(betAmount>=60000 && betAmount<120000){
			this.sentAmount = 18.00;
		}else if(betAmount>=120000 && betAmount<180000){
			this.sentAmount = 26.00;
		}else if(betAmount>=180000 && betAmount<300000){
			this.sentAmount = 36.00;
		}else if(betAmount>=300000 && betAmount<600000){
			this.sentAmount = 68.00;
		}else if(betAmount>=600000 && betAmount<1000000){
			this.sentAmount = 108.00;
		}else if(betAmount>=1000000 && betAmount<2000000){
			this.sentAmount = 388.00;
		}else if(betAmount>=2000000 && betAmount<3000000){
			this.sentAmount = 888.00;
		}else if(betAmount>=3000000){
			this.sentAmount = 3888.00;
		}else{
			this.sentAmount=0.00;
		}
	}
	
	
	public WeekSentVO(Users user, Double amount){
		this.loginname = user.getLoginname();
		this.betAmount = amount;
		if(betAmount>=10000 && betAmount<30000){
			this.sentAmount = 8.00;
		}else if(betAmount>=30000 && betAmount<60000){
			this.sentAmount = 12.00;
		}else if(betAmount>=60000 && betAmount<120000){
			this.sentAmount = 18.00;
		}else if(betAmount>=120000 && betAmount<180000){
			this.sentAmount = 26.00;
		}else if(betAmount>=180000 && betAmount<300000){
			this.sentAmount = 36.00;
		}else if(betAmount>=300000 && betAmount<600000){
			this.sentAmount = 68.00;
		}else if(betAmount>=600000 && betAmount<1000000){
			this.sentAmount = 108.00;
		}else if(betAmount>=1000000 && betAmount<2000000){
			this.sentAmount = 388.00;
		}else if(betAmount>=2000000 && betAmount<3000000){
			this.sentAmount = 888.00;
		}else if(betAmount>=3000000){
			this.sentAmount = 3888.00;
		}else{
			this.sentAmount=0.00;
		}
	}

}
