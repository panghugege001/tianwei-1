package app.model.vo;

public class AgentAccountCenterVO extends BaseVO {
	
	// 代理编号
	private Integer id;
	// 代理等级
	private Integer level;
	// 老虎机佣金余额
	private Double slotAccount;
	private String accountName;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	// 真人佣金余额
	private Double credit;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 本月总输赢
	private Double profitAll;
	// 本月总返水
	private Double ximaFee;
	// 本月总优惠
	private Double couponFee;
	// 本月投注额
	private Double betAll;
	// 会员总人数
	private Integer reg;
	// 本月注册量
	private Integer monthlyReg;
	// 旧密码
	private String oldPassword;
	// 新密码
	private String newPassword;
	// 操作地址
	private String ip;
	// 代理昵称
	private String aliasName;
	// QQ号
	private String qq;
	// 微信号
	private String microChannel;
	// 生日
	private String birthday;
	// 金额
	private Double amount;
	// 卡/折号
	private String bankNo;
	// 密码
	private String password;
	// 银行名称
	private String bankName;
	// 开户网点
	private String bankAddress;
	// 类型
	private String type;
	// 账务类型
	private String accountType;
	// 会员账号
	private String memberName;
	// 存款类型
	private String depositType;
	// 返水类型
	private String waterType;
	// 优惠类型
	private String preferentialType;
	// 平台类型
	private String platformType;
	// 提案类型
	private Integer proposalType;
			
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getSlotAccount() {
		return slotAccount;
	}

	public void setSlotAccount(Double slotAccount) {
		this.slotAccount = slotAccount;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Double getProfitAll() {
		return profitAll;
	}

	public void setProfitAll(Double profitAll) {
		this.profitAll = profitAll;
	}

	public Double getXimaFee() {
		return ximaFee;
	}

	public void setXimaFee(Double ximaFee) {
		this.ximaFee = ximaFee;
	}

	public Double getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Double couponFee) {
		this.couponFee = couponFee;
	}

	public Double getBetAll() {
		return betAll;
	}

	public void setBetAll(Double betAll) {
		this.betAll = betAll;
	}

	public Integer getReg() {
		return reg;
	}

	public void setReg(Integer reg) {
		this.reg = reg;
	}

	public Integer getMonthlyReg() {
		return monthlyReg;
	}

	public void setMonthlyReg(Integer monthlyReg) {
		this.monthlyReg = monthlyReg;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMicroChannel() {
		return microChannel;
	}

	public void setMicroChannel(String microChannel) {
		this.microChannel = microChannel;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
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

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getDepositType() {
		return depositType;
	}

	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}

	public String getWaterType() {
		return waterType;
	}

	public void setWaterType(String waterType) {
		this.waterType = waterType;
	}

	public String getPreferentialType() {
		return preferentialType;
	}

	public void setPreferentialType(String preferentialType) {
		this.preferentialType = preferentialType;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public Integer getProposalType() {
		return proposalType;
	}
	
	public void setProposalType(Integer proposalType) {
		this.proposalType = proposalType;
	}
}