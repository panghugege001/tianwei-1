package dfh.model.enums;

import dfh.utils.StringUtil;

public enum IsCashinType {
	DEPOSIT(0, "有存款"),
	NODEPOSIT(1, "无存款");

	public static String getText(Integer code) {
		IsCashinType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			IsCashinType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	
	public static Integer getCode(String code) {
		IsCashinType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			IsCashinType type = p[i];
			if (StringUtil.equals(type.getText(), code))
				return type.getCode();
		}
		return null;
	}
	
	private Integer code;

	private String text;

	private IsCashinType(Integer code, String text) {
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
