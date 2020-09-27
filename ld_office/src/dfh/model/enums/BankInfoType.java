package dfh.model.enums;

public enum BankInfoType {
	
	CASHOUT("0", "内部账号"),
	CASHIN("1", "外部账号");

	public static String getText(String code) {
		BankInfoType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			BankInfoType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	private String code;

	private String text;

	private BankInfoType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
	
}
