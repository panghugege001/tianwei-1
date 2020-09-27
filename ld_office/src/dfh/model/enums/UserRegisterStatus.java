package dfh.model.enums;

public enum UserRegisterStatus {
	
	UR0(0, "未注册"), UR1(1, "已注册");
	
	public static String getText(Integer code) {
		UserRegisterStatus[] p = values();
		for (int i = 0; i < p.length; ++i) {
			UserRegisterStatus type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private UserRegisterStatus(Integer code, String text) {
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
