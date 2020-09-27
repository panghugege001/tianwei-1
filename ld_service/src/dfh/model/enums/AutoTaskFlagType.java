package dfh.model.enums;

public enum AutoTaskFlagType {
	INIT(0, "初始化"), PROCEED(1, "在生成"), GENERATED(2, "生成完毕"), STOPPED(-1, "已中止"), EXCUTED(3, "已执行");

	public static String getText(Integer code) {
		AutoTaskFlagType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			AutoTaskFlagType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private AutoTaskFlagType(Integer code, String text) {
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
