package dfh.model.enums;

/**
 * 过滤类型
 *
 */
public enum FilterType {
	PHONE("1", "手机"), 
	EMAIL("2", "邮箱");

	private String code;

	private String text;

	private FilterType(String code, String text) {
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
