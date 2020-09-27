package dfh.model.enums;

/**
 * 会员登陆记录、登陆后操作记录标志
 * @author 
 *
 */
public enum ActionLogType {
 
	LOGIN("LOGIN", "登录"),
	LOGIN_GAME("LOGIN_GAME", "登录游戏"),
	LOGIN_AG_GAME("LOGIN_AG_GAME", "登录AG游戏"),
	SERVER_LOGIN("SERVER_LOGIN", "游戏端登录记录"),
	MODIFY_PWD("MODIFY_PWD", "修改密码"),
	MODIFY_CUS_INFO("MODIFY_CUS_INFO", "修改客户资料"), 
	MODIFY_BANK_INFO("MODIFY_BANK_INFO", "修改银行资料"),
	WRONG_PWD("WRONG_PWD", "密码错误"),
	CREDIT_RECORD("CREDIT_RECORD", "额度错误"),
	LOGIN_SPORT_GAME("LOGIN_SPORTBOOK_GAME", "登录体育"),
	SERVICE("SERVICE", "定制服务"),
	MOBILE_REGISTER("MOBILE_REGISTER", "手机注册"),
	PWD_RETRIEVE("PWD_RETRIEVE", "密码找回"),
	PWD_ERROR_5("PWD_ERROR_5", "5次密码错误账户禁用");

	public static String getText(String code) {
		ActionLogType p[] = values();
		for (int i = 0; i < p.length; i++) {
			ActionLogType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;

	private ActionLogType(String code, String text) {
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
