package dfh.model.enums;

public enum CommisionType {
	INIT(1, "待执行"), EXCUTED(0, "已执行");

	public static String getText(Integer code) {
		CommisionType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			CommisionType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private CommisionType(Integer code, String text) {
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