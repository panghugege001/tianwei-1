package dfh.model.enums;

public enum PayOrderFlagType {
	Init(1, "待处理"), FAIL(-1, "失败"), SUCESS(0, "成功");

	public static String getText(Integer code) {
		PayOrderFlagType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PayOrderFlagType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private PayOrderFlagType(Integer code, String text) {
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
