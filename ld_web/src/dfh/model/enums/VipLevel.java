package dfh.model.enums;

public enum VipLevel {
	NEWMEMBER(0, "新会员"), COMMON(1, "忠实vip"), XINGJI(2, "星级vip"), HUANGJIN(3, "黄金vip"),
	BAIJIN(4, "白金vip"),ZUANSHI(5, "钻石vip"),ZHIZUN(6, "至尊vip");

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
