package com.nnti.common.constants;

public enum UserRole {

    MONEY_CUSTOMER("MONEY_CUSTOMER", "真钱会员"),
    AGENT("AGENT", "代理");

	public static String getText(String code) {

		String value = "";
		
		UserRole[] p = values();

		for (int i = 0, len = p.length; i < len; ++i) {

			UserRole type = p[i];

			if (type.getCode().equals(code)) {

				value = type.getText();
				break;
			}
		}

		return value;
	}
	
	//
    private String code;
    //
    private String text;
    
    private UserRole(String code, String text) {
    	
    	this.code = code;
        this.text = text;
    }
    
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}