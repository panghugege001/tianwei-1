package com.nnti.pay.model.vo;

import java.util.Date;

/**
 * Created by wander on 2017/4/5.
 */
public class TotalDeposit {

    private Long id;
    private String loginname;
    private Double allDeposit;
    private Double oneDeposit;
    private Double twoDeposit;
    private Double extend1;
    private Integer extend2;
    private String extend3;
    private Date createTime;// '创建时间'
    private Date updateTime;

    public TotalDeposit() {
    }

    public TotalDeposit(String loginname) {
        this.loginname = loginname;
    }

    public TotalDeposit(String loginname, Double allDeposit, Double oneDeposit, Double twoDeposit, Double extend1, Integer extend2, String extend3, Date createTime, Date updateTime) {
        this.loginname = loginname;
        this.allDeposit = allDeposit;
        this.oneDeposit = oneDeposit;
        this.twoDeposit = twoDeposit;
        this.extend1 = extend1;
        this.extend2 = extend2;
        this.extend3 = extend3;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Double getAllDeposit() {
        return allDeposit;
    }

    public void setAllDeposit(Double allDeposit) {
        this.allDeposit = allDeposit;
    }

    public Double getOneDeposit() {
        return oneDeposit;
    }

    public void setOneDeposit(Double oneDeposit) {
        this.oneDeposit = oneDeposit;
    }

    public Double getTwoDeposit() {
        return twoDeposit;
    }

    public void setTwoDeposit(Double twoDeposit) {
        this.twoDeposit = twoDeposit;
    }

    public Double getExtend1() {
        return extend1;
    }

    public void setExtend1(Double extend1) {
        this.extend1 = extend1;
    }

    public Integer getExtend2() {
        return extend2;
    }

    public void setExtend2(Integer extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
