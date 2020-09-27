package dfh.model.enums;

public enum PlayCodeType {

	DA("10100001", "大"), 
	XIAO("10100002", "小"), 
	DAN("10100003", "单"), 
	SHUANG("10100004", "双"),
	JIN("10100005","金"),
	MU("10100006","木"),
	SHUI("10100007","水"),
	HUO("10100008","火"),
	TU("10100009","土"),
	DADAN("10100010","大单"),
	XIAODAN("10100011","小单"),
	DASHUANG("10100012","大双"),
	XIAOSHUANG("10100013","小双");

	public static String getText(String code) {
		PlayCodeType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PlayCodeType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private PlayCodeType(String code, String text) {
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
