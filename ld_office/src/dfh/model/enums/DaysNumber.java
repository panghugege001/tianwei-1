package dfh.model.enums;

/**
 * 为游戏玩家
 * @author 
 *
 */
public enum DaysNumber {
	Seven(0, "7天"), 
	Fifteen(1, "15天"), 
	Thirty(2, "30天"),
	Thirtymore(3, "30天以上");

	public static String getText(Integer code) {
		DaysNumber[] p = values();
		for (int i = 0; i < p.length; ++i) {
			DaysNumber type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private DaysNumber(Integer code, String text) {
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
