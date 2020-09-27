package dfh.model.enums;

public enum Goddesses {
	BAISHIMOLINAI("BAISHIMOLINAI","白石茉莉奈"),
	CHONGTIANXINGLI("CHONGTIANXINGLI","冲田杏梨"),
	MINGRIHUAQILUO("MINGRIHUAQILUO","明日花綺羅"),
	SHENXIAOSHIZHI("SHENXIAOSHIZHI","神咲诗织"),
	CHAIXIAOSHENG("CHAIXIAOSHENG","柴小圣");
	
	private String name;
	private String code;
	Goddesses(String code, String name){
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	public static String getName(String code) {
		Goddesses[] p = values();
		for (int i = 0; i < p.length; ++i) {
			Goddesses type = p[i];
			if (type.getCode().equals(code))
				return type.getName();
		}
		return null;
	}
}
