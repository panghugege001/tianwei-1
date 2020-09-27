package com.nnti.common.constants;

public enum ActionLogType {

	APP_MACHINE_CODE("APP_MACHINE_CODE", "APP机器码"),
	CREDIT_RECORD("CREDIT_RECORD", "额度错误");

	//
	private String code;
	//
	private String text;

	private ActionLogType(String code, String text) {
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