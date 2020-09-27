package com.nnti.pay.model.vo;

/**
 * Created by wander on 2017/1/30.
 */
public class PayOrderBillNo {
    private String billNo;
    private String loginName;
    private String payPlatform;
    private Double money;
    private String remark;

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setPayPlatform(String payPlatform) {
        this.payPlatform = payPlatform;
    }

    public String getPayPlatform() {
        return payPlatform;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getMoney() {
        return money;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
