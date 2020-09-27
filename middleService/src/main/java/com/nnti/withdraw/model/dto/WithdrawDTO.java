package com.nnti.withdraw.model.dto;

import com.nnti.common.model.dto.CommonDTO;

public class WithdrawDTO extends CommonDTO {
	
	
	private String password;
	private Double money;
	//收款银行
	private String bankName;
	//收款卡号
	private String accountNo;
	//代理提款类型"liveall","slotmachine"
	private String tkType;
	private Integer questionid;
	private String answer;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getTkType() {
		return tkType;
	}
	public void setTkType(String tkType) {
		this.tkType = tkType;
	}
	public Integer getQuestionid() {
		return questionid;
	}
	public void setQuestionid(Integer questionid) {
		this.questionid = questionid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}