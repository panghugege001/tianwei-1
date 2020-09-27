package dfh.model.enums;

public enum GxVoiceEnum {

	CALLING_PD("0", "正在排队等待呼叫"),
	SUCCESS("1", "呼叫成功"),
	FAILED("2", "呼叫失败"),
	EMPTY_CALL("3", "空号"),
	CALLING("4", "正在呼叫中"),
	BUSY("5", "对方忙"),
	OUTOF_SERVICE("6", "关机，不在服务区"),
	NOT_FOUND("404", "id不存在");

	public static String getText(String code) {
		GxVoiceEnum p[] = values();
		for (int i = 0; i < p.length; i++) {
			GxVoiceEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private GxVoiceEnum(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setText(String text) {
		this.text = text;
	}
}
