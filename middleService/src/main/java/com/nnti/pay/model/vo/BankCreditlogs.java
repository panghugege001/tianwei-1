package com.nnti.pay.model.vo;

import java.sql.Timestamp;

public class BankCreditlogs {

    private Integer id;
    private String bankName;
    private Timestamp createTime;
    private String type;
    private Double credit;
    private Double newCredit;
    private Double remit;
    private String remark;

    public BankCreditlogs() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
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