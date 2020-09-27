package dfh.model.enums;


public enum NetpayTypeEnum {
	YEEPAY("yeepay", "yeepay"),
	CNCARD("cncard", "cncard"),
	NPS("nps","nps"),
	IPS("ips","ips"),
	TENPAY("tenpay","tenpay"),
	OPAY("onetopay","1topay");
	
	public static String getText(String code) {
		NetpayTypeEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			NetpayTypeEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private NetpayTypeEnum(String code, String text) {
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
