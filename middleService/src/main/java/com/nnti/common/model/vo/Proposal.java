package com.nnti.common.model.vo;

import java.sql.Timestamp;
import java.util.Date;

public class Proposal {

    private Integer msflag;
    // 提案编号
    private String pno;
    // 操作人
    private String proposer;
    // 创建时间
    private Date createTime;
    // 执行时间
    private Date executeTime;
    //执行用时
    private String timecha;;
    //结束时间
	private Integer overtime;
    // 提案类型
    private Integer type;
    // 记录当前玩家级别
    private Integer quickly;
    // 玩家账号
    private String loginName;
    // 操作金额
    private Double amount;
    // 记录当前玩家所属代理
    private String agent;
    // 操作结果标志(0:待审核/1:已审核/2:已执行/-1:已取消)
    private Integer flag;
    // 操作入口方式(前台/后台)
    private String whereIsFrom;
    // 备注
    private String remark;
    // PT优惠编码
    private String shippingCode;
    // 红利金额
    private Double giftAmount;
    // 流水倍数
    private String betMultiples;
    //
    private String generateType;
    
    private String bankname;
    
    private String saveway;
    
    private String bankaccount;
    
    private Integer mstype;
    private Integer passflag;
    private Integer mssgflag;
	private Double afterLocalAmount;
	private Double afterRemoteAmount; 
    // 提案扩展表对象
    private ProposalExtend proposalExtend;

    public Proposal() {
    }

    /*** 已使用 请勿改 */
    public Proposal(String pno, String proposer, Timestamp createTime, Integer type, Integer quickly, String loginname, Double amount, String agent, Integer flag, String whereisfrom, String remark,
                    String generateType, Integer transferflag) {
        this.pno = pno;
        this.proposer = proposer;
        this.createTime = createTime;
        this.type = type;
        this.quickly = quickly;
        this.loginName = loginname;
        this.amount = amount;
        this.agent = agent;
        this.flag = flag;
        this.whereIsFrom = whereisfrom;
        this.remark = remark;
        this.generateType = generateType;
        this.msflag = transferflag;
    }
    
    
    /** minimal constructor */
	public Proposal(String pno, String proposer, Timestamp createTime, Integer type, Integer quickly, String loginname, Double amount, String agent, Integer flag, String whereisfrom, String remark,
			String generateType) {
		this.pno = pno;
		this.proposer = proposer;
		this.createTime = createTime;
		this.type = type;
		this.quickly = quickly;
		this.loginName = loginname;
		this.amount = amount;
		this.agent = agent;
		this.flag = flag;
		this.whereIsFrom = whereisfrom;
		this.remark = remark;
		this.generateType = generateType;
	}
    

    public String getGenerateType() {
        return generateType;
    }

    public void setGenerateType(String generateType) {
        this.generateType = generateType;
    }

    public String getBetMultiples() {
        return betMultiples;
    }

    public void setBetMultiples(String betMultiples) {
        this.betMultiples = betMultiples;
    }

    public Double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(Double giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }
    
    

    public Double getAfterLocalAmount() {
		return afterLocalAmount;
	}

	public void setAfterLocalAmount(Double afterLocalAmount) {
		this.afterLocalAmount = afterLocalAmount;
	}

	public Double getAfterRemoteAmount() {
		return afterRemoteAmount;
	}

	public void setAfterRemoteAmount(Double afterRemoteAmount) {
		this.afterRemoteAmount = afterRemoteAmount;
	}

	public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }



	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getQuickly() {
        return quickly;
    }

    public void setQuickly(Integer quickly) {
        this.quickly = quickly;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getWhereIsFrom() {
        return whereIsFrom;
    }

    public void setWhereIsFrom(String whereIsFrom) {
        this.whereIsFrom = whereIsFrom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ProposalExtend getProposalExtend() {
        return proposalExtend;
    }

    public void setProposalExtend(ProposalExtend proposalExtend) {
        this.proposalExtend = proposalExtend;
    }

    public Integer getMsflag() {
        return msflag;
    }

    public void setMsflag(Integer msflag) {
        this.msflag = msflag;
    }

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getSaveway() {
		return saveway;
	}

	public void setSaveway(String saveway) {
		this.saveway = saveway;
	}

	public Integer getMstype() {
		return mstype;
	}

	public void setMstype(Integer mstype) {
		this.mstype = mstype;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public Integer getPassflag() {
		return passflag;
	}

	public void setPassflag(Integer passflag) {
		this.passflag = passflag;
	}

	public Integer getMssgflag() {
		return mssgflag;
	}

	public void setMssgflag(Integer mssgflag) {
		this.mssgflag = mssgflag;
	}

	public String getTimecha() {
		return timecha;
	}

	public void setTimecha(String timecha) {
		this.timecha = timecha;
	}

	public Integer getOvertime() {
		return overtime;
	}

	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}
	
	
	
	
}