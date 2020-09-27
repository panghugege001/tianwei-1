package com.nnti.common.constants;

public enum FlashPayFlagEnum {
	FAILED2(-2, "处理失败"), FAILED1(-1, "提交订单失败"), WAIT(0, "待处理"),COMMIT(1,"已提交"),SUCCESS(2,"成功");

	public static String getText(Integer code) {
		FlashPayFlagEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			FlashPayFlagEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private FlashPayFlagEnum(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

}
