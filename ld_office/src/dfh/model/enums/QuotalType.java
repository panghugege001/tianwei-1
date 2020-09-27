package dfh.model.enums;

public enum QuotalType {
	Init(0, "待处理"), SUCESS(1, "成功"), FAIL(2, "已取消");

	public static String getText(Integer code) {
		QuotalType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			QuotalType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private QuotalType(Integer code, String text) {
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
