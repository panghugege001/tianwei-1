package dfh.model.enums;

public enum GamePlatform {

	MGDesktop("MGDesktop", "MG电脑网页游戏"),
	MGHtml5("MGHtml5", "MG手机游戏");

	public static String getText(String code) {
		GamePlatform[] p = values();
		for (int i = 0; i < p.length; ++i) {
			GamePlatform type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private GamePlatform(String code, String text) {
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
