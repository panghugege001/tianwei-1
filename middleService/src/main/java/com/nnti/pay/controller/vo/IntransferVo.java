package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/3/8.
 */
public class IntransferVo {

    private String output;
    private String input;
    private Double amount;
    private Double fee;
    private String remark;
    private Integer transferflag;
    private Integer number;
    private String loginname;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTransferflag() {
        return transferflag;
    }

    public void setTransferflag(Integer transferflag) {
        this.transferflag = transferflag;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
}
