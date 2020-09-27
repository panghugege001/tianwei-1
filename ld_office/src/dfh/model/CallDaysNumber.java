package dfh.model;

/**
 * 为游戏玩家
 * @author 
 *
 */
public enum CallDaysNumber {
	Three(0, "3天以上"),
	Seven(1, "7天以上"), 
	Fifteen(2, "15天以上"), 
	Thirty(3, "30天以上");
//	Thirtymore(3, "30天以上");

	public static String getText(Integer code) {
		CallDaysNumber[] p = values();
		for (int i = 0; i < p.length; ++i) {
			CallDaysNumber type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private CallDaysNumber(Integer code, String text) {
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
