package com.nnti.common.model.vo;

import java.util.Date;

public class CreditLog {

	// 编号 
    private Long id;
    // 玩家账号
    private String loginName;
    // 创建时间 
    private Date createTime;
    // 类型
    private String type;
    // 主账户转账前金额
    private Double credit;
    // 主账户转账后金额
    private Double newCredit;
    // 转账金额 
    private Double remit;
    // 备注
    private String remark;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getNewCredit() {
        return newCredit;
    }

    public void setNewCredit(Double newCredit) {
        this.newCredit = newCredit;
    }

    public Double getRemit() {
        return remit;
    }

    public void setRemit(Double remit) {
        this.remit = remit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}