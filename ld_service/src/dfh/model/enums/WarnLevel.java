package dfh.model.enums;

/**
 * 会员类型(等级)
 * @author 
 *
 */
public enum WarnLevel {
	WEIZHI(0, "未知"), KEYI(1, "可疑"), WEIXIAN(2, "危险"),ANQUAN(3,"安全"),WUXIAO(4,"无效");

	public static String getText(Integer code) {
		WarnLevel[] p = values();
		for (int i = 0; i < p.length; ++i) {
			WarnLevel type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private WarnLevel(Integer code, String text) {
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
