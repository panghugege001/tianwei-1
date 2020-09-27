package com.nnti.common.model.vo;

import java.util.Date;

public class User {

    // 玩家登录账号
    private String loginName;
    // 玩家登录密码
    private String password;
    // 玩家账号余额
    private Double credit;
    // 玩家等级
    private Integer level;
    // 玩家所属代理账号
    private String agent;
    // 玩家姓名
    private String accountName;
    // 玩家电话号码
    private String phone;
    // 玩家状态(0:启用/1:禁用)
    private Integer flag;
    // 玩家昵称
    private String aliasName;
    // 玩家邮箱
    private String email;
    // 玩家创建时间
    private Date createTime;
    // 玩家类型(MONEY_CUSTOMER:真钱会员/AGENT:代理)
    private String role;
    // 当日最高转账金额，-1表示转账没有限制/0表示不能转账/1000表示一天最高转账1000
    private Double creditLimit;
    // 当日已转账金额
    private Double creditDay;
    // 转账日期
    private String creditDayDate;
    // 是否有过充值(0:有过充值/1:从未充值)
    private Integer isCashin;
    // 警告级别(0:未知/1:可疑/2:危险/3:安全/4:无效)
    private Integer warnFlag;
    // 玩家注册IP
    private String registerIp;
    // PT优惠编码
    private String shippingCodePt;
    // 会话编码
    private String postCode;
    // 出生日期
    private Date birthday;
    /*** 用户推荐码 */
    private String intro;
    /*** 代理推荐码 */
    private String partner;
    
    private String remark;

    public User() {}
    
    public User(String loginName, Integer isCashin) {
        
    	this.loginName = loginName;
        this.isCashin = isCashin;
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

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getCreditDay() {
        return creditDay;
    }

    public void setCreditDay(Double creditDay) {
        this.creditDay = creditDay;
    }

    public String getCreditDayDate() {
        return creditDayDate;
    }

    public void setCreditDayDate(String creditDayDate) {
        this.creditDayDate = creditDayDate;
    }

    public void setIsCashin(Integer isCashin) {
        this.isCashin = isCashin;
    }

    public Integer getIsCashin() {
        return isCashin;
    }

	public Integer getWarnFlag() {
		return warnFlag;
	}

	public void setWarnFlag(Integer warnFlag) {
		this.warnFlag = warnFlag;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getShippingCodePt() {
		return shippingCodePt;
	}
	
	public void setShippingCodePt(String shippingCodePt) {
		this.shippingCodePt = shippingCodePt;
	}

	public String getPostCode() {
		return postCode;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}