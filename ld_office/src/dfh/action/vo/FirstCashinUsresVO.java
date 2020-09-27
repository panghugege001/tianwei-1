package dfh.action.vo;

import java.io.Serializable;

public class FirstCashinUsresVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -305892972721321154L;
	
	private String loginName;		// 登录ID
	private String aliasName;		// 别名
	private String accountName;		// 真实姓名
	private double firstAmount;		// 首存金额
	private String registerTime;	// 注册时间
	private String firstTime;		// 首存时间
	private String spaceDays;		// 首存和注册时间的间隔天数
	private String referWebsite;	// 代理网址
	private double concessionsAmount;// 优惠金额
	private String howToKnow;        //来源网址
	
	
	
	
	public double getConcessionsAmount() {
		return this.concessionsAmount;
	}


	public void setConcessionsAmount(double concessionsAmount) {
		this.concessionsAmount = concessionsAmount;
	}


	public FirstCashinUsresVO(String loginName, String aliasName,
			String accountName, double firstAmount, String registerTime,
			String firstTime, String referWebsite) {
		super();
		this.loginName = loginName;
		this.aliasName = aliasName;
		this.accountName = accountName;
		this.firstAmount = firstAmount;
		this.registerTime = registerTime;
		this.firstTime = firstTime;
		this.referWebsite = referWebsite;
	}


	public FirstCashinUsresVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getLoginName() {
		return loginName;
	}




	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}




	public String getAliasName() {
		return aliasName;
	}




	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}




	public String getAccountName() {
		return accountName;
	}




	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}




	public double getFirstAmount() {
		return firstAmount;
	}




	public void setFirstAmount(double firstAmount) {
		this.firstAmount = firstAmount;
	}




	public String getRegisterTime() {
		return registerTime;
	}




	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}




	public String getFirstTime() {
		return firstTime;
	}




	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}




	public String getSpaceDays() {
		return spaceDays;
	}




	public void setSpaceDays(String spaceDays) {
		this.spaceDays = spaceDays;
	}




	public String getReferWebsite() {
		return referWebsite;
	}




	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}


	public String getHowToKnow() {
		return howToKnow;
	}


	public void setHowToKnow(String howToKnow) {
		this.howToKnow = howToKnow;
	}

	
	

	
	
	

}
