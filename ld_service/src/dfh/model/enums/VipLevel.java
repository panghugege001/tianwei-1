package dfh.model.enums;

public enum VipLevel {
	TIANBING(0, "天兵"), 
	TIANJIANG(1, "天将"), 
	TIANWANG(2, "天王"), 
	XINGJUN(3, "星君"), 
	ZHENJUN(4, "真君"), 
	XIANJUN(5,"仙君"), 
	DIJUN(6, "帝君"),
	TIANZUN(7, "天尊"),
	TIANDI(8, "天帝"),
	;

	public static String getText(Integer code) {
		VipLevel[] p = values();
		for (int i = 0; i < p.length; ++i) {
			VipLevel type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private VipLevel(Integer code, String text) {
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
