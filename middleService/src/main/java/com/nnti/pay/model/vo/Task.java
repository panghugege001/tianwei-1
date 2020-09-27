package com.nnti.pay.model.vo;

import java.sql.Timestamp;

/**
 * Created by wander on 2017/3/8.
 */
public class Task {
    private Integer id;
    private String pno;
    private String level;
    private Integer flag;
    private Timestamp agreeTime;
    private String agreeIp;
    private String manager;

    public Task() {
    }

    public Task(String pno, String level, Integer flag, Timestamp agreeTime, String agreeIp, String manager) {
        this.pno = pno;
        this.level = level;
        this.flag = flag;
        this.agreeTime = agreeTime;
        this.agreeIp = agreeIp;
        this.manager = manager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Timestamp getAgreeTime() {
        return agreeTime;
    }

    public void setAgreeTime(Timestamp agreeTime) {
        this.agreeTime = agreeTime;
    }

    public String getAgreeIp() {
        return agreeIp;
    }

    public void setAgreeIp(String agreeIp) {
        this.agreeIp = agreeIp;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
