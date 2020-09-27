package dfh.model.enums;

public enum UserLimitType {
	
	LIMIT(-1, "无限制"), LIMIT0(0, "0"), LIMIT1000(1000, "1000"), LIMIT5000(5000, "5000");

	public static String getText(Integer code) {
		UserLimitType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			UserLimitType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private UserLimitType(Integer code, String text) {
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
