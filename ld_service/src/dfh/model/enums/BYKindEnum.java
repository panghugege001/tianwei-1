package dfh.model.enums;

public enum BYKindEnum {
	XY_FISH("411", "西游捕鱼"), BYDS_FISH("105", "捕鱼大师"),LIVE_BG("","BG真人");

	BYKindEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
