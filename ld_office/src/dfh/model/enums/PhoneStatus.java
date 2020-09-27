package dfh.model.enums;

public enum PhoneStatus {
	
	PHONE0(0, "未拨打"), PHONE1(1, "正常"), PHONE2(2, "无人接听"),PHONE3(3, "空号"),PHONE4(4, "可用");

	public static String getText(Integer code) {
		PhoneStatus[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PhoneStatus type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private PhoneStatus(Integer code, String text) {
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
