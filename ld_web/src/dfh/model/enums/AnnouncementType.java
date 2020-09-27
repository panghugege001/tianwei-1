package dfh.model.enums;

public enum AnnouncementType {
	INDEX("INDEX", "首页公告"),INDEX_TOP("INDEX_TOP","首页置顶公告");

	public static String getText(String code) {
		AnnouncementType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			AnnouncementType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private AnnouncementType(String code, String text) {
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
