package dfh.model.enums;

public enum AutoGenerateType {
	AUTO_XIMA("AUTO_XIMA", "自动结算洗码"), AUTO_CMN("AUTO_CMN", "自动结算佣金");

	public static String getText(String code) {
		AutoGenerateType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			AutoGenerateType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private AutoGenerateType(String code, String text) {
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
