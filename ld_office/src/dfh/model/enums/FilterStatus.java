package dfh.model.enums;

/**
 * 过滤状态 
 *
 */
public enum FilterStatus {
	Y("Y", "生效"), 
	N("N", "失效");

	private String code;

	private String text;

	private FilterStatus(String code, String text) {
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
