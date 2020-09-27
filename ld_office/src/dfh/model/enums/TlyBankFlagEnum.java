package dfh.model.enums;

/**
 * 点卡类型
 *
 * @author
 */
public enum TlyBankFlagEnum {
	ABC("ABC", "农业银行"), 
	ICBC("ICBC", "工商银行"),
	BCM("BCM", "交通银行"),
	CCB("CCB", "建设银行"),
	CMB("CMB", "招商银行"),
	CMBC("CMBC", "民生银行"),
	HXB("HXB", "华夏银行"),
	PSBC("PSBC", "邮政银行"),
	CNCB("CNCB", "中信银行"),
	CIB("CIB", "兴业银行"),
	PAB("PAB", "平安银行"),
	SPDB("SPDB", "浦发银行"),
	NINEPAY("NINEPAY", "九付宝"),
	LEPOS("LEPOS", "乐刷银行"),
	CGB("CGB", "广发银行"),
	HRBB("HRBB", "哈尔滨银行"),
	CZB("CZB", "浙商银行");

	public static String getText(String code) {
		TlyBankFlagEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			TlyBankFlagEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	
	public static String getCode(String text) {
		TlyBankFlagEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			TlyBankFlagEnum type = p[i];
			if (type.getText().equals(text))
				return type.getCode();
		}
		return null;
	}
	

	private String code;

	private String text;

	private TlyBankFlagEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

	public static TlyBankFlagEnum getCardType(Integer code) {
		return TlyBankFlagEnum.values()[code];
	}
}
