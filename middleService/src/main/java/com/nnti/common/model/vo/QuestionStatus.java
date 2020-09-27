package com.nnti.common.model.vo;

import java.util.Date;

public class QuestionStatus implements java.io.Serializable {

	// Fields
	private String loginname;
	private Integer errortimes;
	private Date createtime;
	private Date updatetime;
	private String remark;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getErrortimes() {
		return errortimes;
	}
	public void setErrortimes(Integer errortimes) {
		this.errortimes = errortimes;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}