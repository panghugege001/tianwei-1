package dfh.model.enums;

public enum TransType {
	CASHIN("0001", "转入"), 
	CASHOUT("0002", "转出"), 
	BUY("0004", "交易(投注)"), 
	BUYIN("0005", "交易结算(投注)"),
	ORDERRESET("0020", "订单重算(投注)"),
	CANCELORDER("0030", "取消订单(投注)");

	public static String getText(String code) {
		TransType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			TransType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private String code;

	private String text;

	private TransType(String code, String text) {
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
