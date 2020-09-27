package com.nnti.Privilege.model.vo;


/**
 * Prize entity. @author MyEclipse Persistence Tools
 */
public class Prize {

	// Fields
	private String pno;
	private String title;
	private String loginname;
	private Double tryCredit;
	private String remark;

	// Constructors

	/** default constructor */
	public Prize() {
	}

	/** minimal constructor */
	public Prize(String pno, String title, String loginname, Double tryCredit, String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.tryCredit = tryCredit;
		this.remark = remark;
	}

	// Property accessors
	public String getPno() {
		return this.pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getTryCredit() {
		return this.tryCredit;
	}

	public void setTryCredit(Double tryCredit) {
		this.tryCredit = tryCredit;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}