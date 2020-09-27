package dfh.action.vo;

import java.io.Serializable;

import dfh.model.Users;

public class ProfitVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2440530889440093091L;
	private String loginname;//帐号
	private Double profitAmouont;//负盈利反赠金额
	private Double validAmount;//ea后台输赢值
	private Integer level;//用户等级
	
	Double rate;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getProfitAmouont() {
		return profitAmouont;
	}
	public void setProfitAmouont(Double profitAmouont) {
		this.profitAmouont = profitAmouont;
	}
	public Double getValidAmount() {
		return validAmount;
	}
	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	public ProfitVO(String loginname, Double validAmount,Integer level) {
		super();
		this.loginname = loginname;
		this.validAmount = validAmount;
		this.level = level;
		this.rate=0.40;
		
		if(level==2){
			this.rate=0.06;
			this.profitAmouont=Math.abs(this.validAmount*0.06)>26666?26666:Math.abs(this.validAmount*0.06);
		}else if(level==3){
			this.rate=0.07;
			this.profitAmouont=Math.abs(this.validAmount*0.07)>26666?26666:Math.abs(this.validAmount*0.07);
		}else if(level==4){
			this.rate=0.08;
			this.profitAmouont=Math.abs(this.validAmount*0.08)>26666?26666:Math.abs(this.validAmount*0.08);
		}else if(level==5){
			this.rate=0.09;
			this.profitAmouont=Math.abs(this.validAmount*0.09)>26666?26666:Math.abs(this.validAmount*0.09);
		}else if(level==6){
			this.profitAmouont=Math.abs(this.validAmount*0.10)>26666?26666:Math.abs(this.validAmount*0.10);
		}else if(level==7){
			this.profitAmouont=Math.abs(this.validAmount*0.10)>26666?26666:Math.abs(this.validAmount*0.10);
		}else if(level==8){
			this.profitAmouont=Math.abs(this.validAmount*0.10)>26666?26666:Math.abs(this.validAmount*0.10);
		}
	}
	
	public ProfitVO(Users user, Double amount){
		this.loginname = user.getLoginname();
		this.validAmount = amount;
		this.level = user.getLevel();
		if(level.equals(0) ){
			this.rate=0.04;
		}else if(level.equals(1)){
			this.rate=0.05;
		}else if(level.equals(2)){
			this.rate=0.06;
		}else if(level.equals(3)){
			this.rate=0.07;
		}else if(level.equals(4)){
			this.rate=0.08;
		}else if(level.equals(5)){
			this.rate=0.09;
		}else if(level.equals(6)){
			this.rate=0.10;
		}else if(level.equals(7)){
			this.rate=0.12;
		}else if(level.equals(8)){
			this.rate=0.15;
		}else{
			this.rate=0.00;
		}
		this.profitAmouont = Math.abs(this.validAmount*rate);
	}

}
