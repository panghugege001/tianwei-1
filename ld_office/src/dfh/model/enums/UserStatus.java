package dfh.model.enums;

public enum UserStatus {
	
	US0(0, "未处理"), US1(1, "良好"), US2(2, "一般"),US3(3, "拒绝");

	public static String getText(Integer code) {
		UserStatus[] p = values();
		for (int i = 0; i < p.length; ++i) {
			UserStatus type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private UserStatus(Integer code, String text) {
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
