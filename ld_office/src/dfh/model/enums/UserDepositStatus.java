package dfh.model.enums;

public enum UserDepositStatus {
	
	USERDEPOSIT0(0, "未存款"), USERDEPOSIT1(1, "已存款");
	
	public static String getText(Integer code) {
		UserDepositStatus[] p = values();
		for (int i = 0; i < p.length; ++i) {
			UserDepositStatus type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private UserDepositStatus(Integer code, String text) {
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
