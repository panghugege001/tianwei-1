package com.nnti.common.model.vo;

import java.util.Date;

public class AgentVip {

	// Fields
	private String  agent ;
	private Integer level ;
	private Integer activemonth;
	private Integer activeuser;
	private Date registertime;
	private Date createtime;
	private String remark;
	private Double historyfee ;
	
	
	

	
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getActivemonth() {
		return activemonth;
	}
	public void setActivemonth(Integer activemonth) {
		this.activemonth = activemonth;
	}
	public Integer getActiveuser() {
		return activeuser;
	}
	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}
	public Date getRegistertime() {
		return registertime;
	}
	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getHistoryfee() {
		return historyfee;
	}
	public void setHistoryfee(Double historyfee) {
		this.historyfee = historyfee;
	}
	
	
}