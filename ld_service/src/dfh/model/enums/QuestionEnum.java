package dfh.model.enums;

public enum QuestionEnum {
	QUESTION1(1, "您最喜欢的明星名字？"),
	QUESTION2(2, "您最喜欢的职业？"),
	QUESTION3(3, "您最喜欢的城市名称？"),
	QUESTION4(4, "对您影响最大的人名字是？"),
	QUESTION5(5, "您就读的小学名称？"),
	QUESTION6(6, "您最熟悉的童年好友名字是？");

	public static String getText(Integer code) {
		QuestionEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			QuestionEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	public static Boolean existKey(Integer code) {
		QuestionEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			QuestionEnum type = p[i];
			if (type.getCode().equals(code))
				return true;
		}
		return false;
	}

	private Integer code;

	private String text;

	private QuestionEnum(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
	
	public static void main(String[] args) {
		System.out.println(existKey(7));;
	}
}
