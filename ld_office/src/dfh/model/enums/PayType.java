package dfh.model.enums;

public enum PayType {
	Init(1, "待处理"), FAIL(-1, "已取消"), SUCESS(0, "成功"),WESUCESS(2, "未支付");

	public static String getText(Integer code) {
		PayType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PayType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private PayType(Integer code, String text) {
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
