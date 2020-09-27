package dfh.model.enums;

public enum DateIntervalType {
	TODAY(1,"今天"),
	WEEK(7, "一星期"),FIFTEENDAY(15,"半个月"),ONEMONTH(30, "一个月"),
	TWOMONTH(60, "两个月"), SIXMONTH(180, "半年"),ONEYEAR(365, "一年");

	public static String getText(Integer code) {
		DateIntervalType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			DateIntervalType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private DateIntervalType(Integer code, String text) {
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
