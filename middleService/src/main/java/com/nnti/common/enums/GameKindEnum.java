package com.nnti.common.enums;

public enum GameKindEnum {
	DZ("DZ", "电子"), QP("QP", "棋牌"), LIVE("LIVE", "真人"), CP("CP", "彩票"), BY("BY", "捕鱼"), DJ("DJ", "电竞"), SP("SP", "体育");

	GameKindEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
