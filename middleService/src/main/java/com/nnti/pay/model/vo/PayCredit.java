package com.nnti.pay.model.vo;

import java.util.Date;

/**
 * Created by wander on 2017/2/22.
 * 支付平台流水表
 */
public class PayCredit {

    private String payOrderNo;//'支付订单号',
    private String loginname;// '用户名',
    private String agentName;//'代理名称',
    private String intro;//'用户推荐码',
    private String partner;// '代理推荐码',
    private Date userCreateTime;// '用户注册时间',
    private String payPlatform;// '支付平台',
    private Integer payWay;//'支付方式 1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付',
    private Double deposit;// '用户存款',
    private Integer firstDeposit;// '是否首存  1.首存 0 不是收存',
    private Double userFee;//'支付手续费 ',
    private Double userGet;// '扣除手续费后的用户存款',
    private Double bankFee;// '银行手续费',
    private Double bankGet;// '扣除手续费后的存款银行存款',
    private String remark;// '备注',
    private Date createTime;// '创建时间'

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public String getPayPlatform() {
        return payPlatform;
    }

    public void setPayPlatform(String payPlatform) {
        this.payPlatform = payPlatform;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Integer getFirstDeposit() {
        return firstDeposit;
    }

    public void setFirstDeposit(Integer firstDeposit) {
        this.firstDeposit = firstDeposit;
    }

    public Double getUserFee() {
        return userFee;
    }

    public void setUserFee(Double userFee) {
        this.userFee = userFee;
    }

    public Double getUserGet() {
        return userGet;
    }

    public void setUserGet(Double userGet) {
        this.userGet = userGet;
    }

    public Double getBankFee() {
        return bankFee;
    }

    public void setBankFee(Double bankFee) {
        this.bankFee = bankFee;
    }

    public Double getBankGet() {
        return bankGet;
    }

    public void setBankGet(Double bankGet) {
        this.bankGet = bankGet;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
