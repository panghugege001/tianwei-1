package dfh.model.enums;

public enum IssuingBankNumber {

	INDUSTRIAL_AND_COMMERCIAL_BANK_OF_CHINA("工商银行", "XXXXXXXXXXXXXXXX"),
	AGRICULTURAL_BANK_OF_CHINA("农业银行", "XXXXXXXXXXXXXXXX"), 
	CHINA_CONSTRUCTION_BANK("建设银行", "XXXXXXXXXXXXXXXX"),
	BANK_OF_COMMUNICATIONS("交通银行", "XXXXXXXXXXXXXXXX");
	
	public static String getText(String code) {
		IssuingBankNumber[] p = values();
		for (int i = 0; i < p.length; ++i) {
			IssuingBankNumber type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private IssuingBankNumber(String code, String text) {
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
