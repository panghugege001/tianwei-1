package com.nnti.common.model.vo;



/**
 * Bankinfo entity. @author MyEclipse Persistence Tools
 */

public class BankCardinfo {
	
	private Integer id;
	
	private String issuebankname;
	
	private String cardname;
	
	private Integer cardlength;
	
	private String masteraccount;
	
	private Integer identifylegth;
	
	private String identifycode;
	
	private String  cardtype;
	
	
	
	
	/** default constructor */
	public BankCardinfo() {
	}

	public BankCardinfo(Integer id,String issuebankname,  String cardname, Integer cardlength ,String masteraccount,Integer identifylegth,String identifycode,String  cardtype) {
		super();
		this.id = id;
		this.issuebankname = issuebankname;
		this.cardname = cardname;
		this.cardlength = cardlength;
		this.masteraccount = masteraccount;
		this.identifylegth = identifylegth;
		this.identifycode = identifycode;
		this.cardtype = cardtype;
	}
	
	// Property accessors

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getIssuebankname() {
		return issuebankname;
	}

	public void setIssuebankname(String issuebankname) {
		this.issuebankname = issuebankname;
	}
	

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}


	public Integer getCardlength() {
		return cardlength;
	}

	public void setCardlength(Integer cardlength) {
		this.cardlength = cardlength;
	}


	public String getMasteraccount() {
		return masteraccount;
	}

	public void setMasteraccount(String masteraccount) {
		this.masteraccount = masteraccount;
	}

	

	public Integer getIdentifylegth() {
		return identifylegth;
	}

	public void setIdentifylegth(Integer identifylegth) {
		this.identifylegth = identifylegth;
	}


	public String getIdentifycode() {
		return identifycode;
	}

	public void setIdentifycode(String identifycode) {
		this.identifycode = identifycode;
	}


	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	

}
