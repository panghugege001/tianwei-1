package com.nnti.pay.model.vo;

import java.util.Date;

/**
 * Created by wander on 2017/2/6.
 */
public class DepositOrder {

	private String depositId;
    private String loginName;
    private String bankName;
    private String bankNo;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private String spare;
    private String spare1;
    private Double amount;
    private String accountName;
    private String ubankname;
    private String uaccountname;
    private String ubankno;
    private Integer flag;
    private String type;
    private String realname;
    

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }


	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}



	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}

	public String getSpare1() {
		return spare1;
	}

	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}

	public String getUbankname() {
		return ubankname;
	}

	public void setUbankname(String ubankname) {
		this.ubankname = ubankname;
	}

	public String getUaccountname() {
		return uaccountname;
	}

	public void setUaccountname(String uaccountname) {
		this.uaccountname = uaccountname;
	}

	public String getUbankno() {
		return ubankno;
	}

	public void setUbankno(String ubankno) {
		this.ubankno = ubankno;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	


    
    
}
