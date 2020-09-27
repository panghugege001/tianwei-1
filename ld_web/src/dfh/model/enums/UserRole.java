package dfh.model.enums;

public enum UserRole {
//	FREE_CUSTOMER("FREE_CUSTOMER", "试玩会员"),
	MONEY_CUSTOMER("MONEY_CUSTOMER", "真钱会员"),
	AGENT("AGENT", "代理");
//	PARTNER("PARTNER", "合作伙伴");

	public static String getText(String code) {
		UserRole[] p = values();
		for (int i = 0; i < p.length; ++i) {
			UserRole type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private UserRole(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
}