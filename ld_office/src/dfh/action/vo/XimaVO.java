package dfh.action.vo;

import java.io.Serializable;

public class XimaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3672132535865475265L;
	private Double validBetAmount;
	private String loginname;
	private Double ximaAmouont;
	public Double getXimaAmouont() {
		return ximaAmouont;
	}
	public void setXimaAmouont(Double ximaAmouont) {
		this.ximaAmouont = ximaAmouont;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	Double rate;
	
	// 洗码率
	public Double getXimaRate(Double validaBetAmount) {
		
		if (validaBetAmount <= 100 * 10000) {
			//return 0.0045;
			return 0.008;
//		} else if (validaBetAmount > 50 * 10000 && validaBetAmount <= 100 * 10000) {
//			rate = 0.004;
		} else if (validaBetAmount > 100 * 10000 && validaBetAmount <= 300 * 10000) {
			//return 0.006;
			return 0.008;
		} else if (validaBetAmount > 300 * 10000 && validaBetAmount <= 500 * 10000) {
			//return 0.008;
			return 0.008;
		} else{
			return 0.008;
		}
	}

	public XimaVO(Double validBetAmount, String loginname) {
		super();
		this.validBetAmount = validBetAmount;
		this.loginname = loginname;
		this.rate=this.getXimaRate(validBetAmount);
		this.ximaAmouont=this.validBetAmount*this.rate>80000?80000:this.validBetAmount*this.rate;
	}
	public XimaVO(Double validBetAmount, String loginname,Double rate,Double rebate) {
		super();
		this.validBetAmount = validBetAmount;
		this.loginname = loginname;
		this.rate=rate;
		//this.rate=this.getXimaRate(validBetAmount);
		//this.ximaAmouont=this.validBetAmount*this.rate>28888?28888:this.validBetAmount*this.rate;
		this.ximaAmouont=this.validBetAmount*this.rate>rebate?rebate:this.validBetAmount*this.rate;
	}
	public XimaVO(Double validBetAmount, String loginname,Double rate) {
		super();
		this.validBetAmount = validBetAmount;
		this.loginname = loginname;
		this.rate=rate;
		//this.rate=this.getXimaRate(validBetAmount);
		//this.ximaAmouont=this.validBetAmount*this.rate>28888?28888:this.validBetAmount*this.rate;
		this.ximaAmouont=this.validBetAmount*this.rate;
	}
	public Double getValidBetAmount() {
		return validBetAmount;
	}
	public void setValidBetAmount(Double validBetAmount) {
		this.validBetAmount = validBetAmount;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	

}
