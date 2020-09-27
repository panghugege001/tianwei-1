package app.model.vo;

public class WithdrawalVO {

	// 玩家账号
	private String loginName;
	// 登录密码
	private String password;
	// 取款银行
	private String bankName;
	// 银行卡号
	private String bankNo;
	// 银行网址
	private String bankAddress;
	// 取款金额
	private Double money;
	// 密保问题
	private Integer questionId;
	// 密保答案
	private String answer;
	// 提款类型
	private String type;
	// 操作IP地址
	private String ip;
	
	private String sid;
	
	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}