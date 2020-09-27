package dfh.model.enums.shaba;

//赔率
public enum OddsType {

	Malay("1", "a"), //马来赔率
	Hong_Kong("2", "hka"), //香港赔率
	Decimal("3", "de"), //十进制赔率
	Indo("4", "indo"), //印度赔率
	American("5", "us");//美国赔率

	public static String getText(String code) {
		OddsType p[] = values();
		for (int i = 0; i < p.length; i++) {
			OddsType type = p[i]; 
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private OddsType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public void setCode(String code) {
		this.code = code; 
	}

	public void setText(String text) {
		this.text = text;
	}
}
