package com.nnti.common.model.vo;

import java.sql.Timestamp;

/**
 * Created by wander on 2017/3/9.
 */
public class OperationLog {

    private Integer id;
    private String loginname;
    private String action;
    private Timestamp createtime;
    private String remark;

    public OperationLog() {
    }

    public OperationLog(String loginname, String action, Timestamp createtime, String remark) {
        this.loginname = loginname;
        this.action = action;
        this.createtime = createtime;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
