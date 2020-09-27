package dfh.model.enums;

public enum OperatorRole {
	ADMIN("ADMIN", "管理员"),
	SALE("SALE", "客服"),
	FINANCE("FINANCE", "财务"), 
	MANAGER("MANAGER", "经理");

	public static String getText(String code) {
		OperatorRole[] p = values();
		for (int i = 0; i < p.length; ++i) {
			OperatorRole type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private OperatorRole(String code, String text) {
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