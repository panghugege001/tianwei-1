package app.enums;


/**
 * 专题优惠 长期优惠 限时优惠 其他优惠
 * @author joey
 *
 */
public enum LatestPreferentialTypeEnums {
	ALL("000", "全部优惠"),
	TOPIC("001", "专题优惠"),
	LONG_DATA("002", "长期优惠"),
	LIMIT_DATA("003", "限时优惠"),
	FORUM("004", "其他优惠");
	
	private LatestPreferentialTypeEnums(String code, String text) {
	
		this.code = code;
		this.text = text;
	}

	private String code;
	private String text;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	
	public static String getValueByCode(String code) {
		LatestPreferentialTypeEnums p[] = values();
		for (int i = 0; i < p.length; i++) {
			LatestPreferentialTypeEnums type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
}