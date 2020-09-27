package dfh.model.enums;

public enum GameType {
	BEIJING("01", "北京"), 
	SHENZHEN("03", "深圳"), 
	JNDBS("04", "加拿大卑斯"), 
	JNAXB("05", "加拿大西部"), 
	SILUOFAKE("06", "斯洛伐克"),
	AOZHOU("08", "澳洲");

	public static String getText(String code) {
		GameType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			GameType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private GameType(String code, String text) {
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
