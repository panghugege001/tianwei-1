package com.nnti.common.constants;

public enum NTwoXmlEnum {

	ACTION("action"), 
	CLOGIN("clogin"), 
	USERVERF("userverf"), 
	CGETTICKET("cgetticket"), 
	CDEPOSIT("cdeposit"), 
	CDEPOSIT_CONFIEM("cdeposit-confirm"), 
	CWITHDRAWAL("cwithdrawal"), 
	CCHECKCLIENT("ccheckclient"), 
	CCHECKAFFILIATE("ccheckaffiliate"), 
	GAMEINFO("gameinfo");

	private String action;

	private NTwoXmlEnum(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}
}