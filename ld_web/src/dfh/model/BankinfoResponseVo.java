package dfh.model;

import java.util.List;


/**
 * Created by Addis on 2017/10/12.
 */
public class BankinfoResponseVo {

	//返回信息
	private String massage;
	
	//返回附言 
	private String depositId;

	//开户地区
	private String area;

	//返回金额
	private String amount;
	
	//返回二维码
	private String zfbImgCode;
	
	//返回银行卡号
	private String accountno;
	
	//收款姓名
	private String username;
	
	//收款银行
	private String bankname;
	
	//账户类型 （存款账户，支付账户等）
	private Integer type;
	
	//卡种类(0 网银 1 支付宝 2 3 微信 )
	private Integer paytype;
	
	//当为true时，前端会询问玩家是否删除旧订单，生成新订单。
	private boolean force;
	
	
	
	

	public String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}

	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getZfbImgCode() {
		return zfbImgCode;
	}

	public void setZfbImgCode(String zfbImgCode) {
		this.zfbImgCode = zfbImgCode;
	}

	public String getAccountno() {
		return accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
	
	



    
}
