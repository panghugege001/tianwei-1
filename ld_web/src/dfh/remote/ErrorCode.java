package dfh.remote;

public enum ErrorCode {
	SUCCESS("0", "SUCCESS", "成功"), ERROR_INVALID_ACCOUNT_ID("101",
			"ERROR_INVALID_ACCOUNT_ID", "无效的帐号"), ERROR_ALREADY_LOGIN("102",
			"ERROR_ALREADY_LOGIN", "用户已登录"), ERROR_DATABASE_ERROR("103",
			"ERROR_DATABASE_ERROR", "数据库错误"), ERROR_ACCOUNT_SUSPENDED("104",
			"ERROR_ACCOUNT_SUSPENDED", "用户被禁用"), ERROR_INVALID_CURRENCY("105",
			"ERROR_INVALID_CURRENCY", "无效的货币值"), ERR_INVALID_REQ("201",
			"ERR_INVALID_REQ", "无效的请求"), ERR_DB_OPEATION("202",
			"ERR_DB_OPEATION", "错误的数据操作"), ERR_INVAILD_CLIENT("203",
			"ERR_INVAILD_CLIENT", "客户用户ID无效"), ERR_EXCEED_AMOUNT("204",
			"ERR_EXCEED_AMOUNT", "超过最大金额"), ERR_INVAILD_VENDOR("205",
			"ERR_INVAILD_VENDOR", "非法代理"), ERR_LOCKED_CLIENT("206",
			"ERR_LOCKED_CLIENT", "帐户在客户端被锁定"), ERR_INVAILD_PROMOTION_WITHDRAWAL(
			"210", "ERR_INVAILD_PROMOTION_WITHDRAWAL",
			"ERR_INVAILD_PROMOTION_WITHDRAWAL"), ERR_CLIENT_IN_GAME("211",
			"ERR_CLIENT_IN_GAME", "客户在游戏中"), ERR_MAX_xxx_WITHDRAWAL_DAILY(
			"212", "ERR_MAX_xxx_WITHDRAWAL_DAILY", "超出每日最大转出次数"), ERR_PLAYTHROUGHFACTOR_NOTREACH(
			"213", "ERR_PLAYTHROUGHFACTOR_NOTREACH",
			"ERR_PLAYTHROUGHFACTOR_NOTREACH"), ERR_DUPLICATE_REFNO("401",
			"ERR_DUPLICATE_REFNO", "重复的流水号"), ERR_INVAILD_PREFIX_ERR_INVAILD_IP(
			"402", "ERR_INVAILD_PREFIX|ERR_INVAILD_IP", "非法的前缀或IP"), ERR_INVAILD_AMOUNT(
			"403", "ERR_INVAILD_AMOUNT", "非法的金额"), ERR_ILLEGAL_DECIMAL("404",
			"ERR_ILLEGAL_DECIMAL", "非法的十进制"), ERR_INVALID_ACODE("501",
			"ERR_INVALID_ACODE", "非法的代理编号"), ERR_INVALID_BEGINDATE("502",
			"ERR_INVALID_BEGINDATE", "非法的开始日期"), ERR_INVALID_ENDDATE("503",
			"ERR_INVALID_ENDDATE", "非法的结束日期"), ERR_INVALID_ENDDATELOWBEGINDATE(
			"504", "ERR_INVALID_ENDDATELOWBEGINDATE", "结束日期在开始日期之前"), ERR_INVALID_RETURN_ACODE(
			"505", "ERR_INVALID_RETURN_ACODE", "非法的返回代理编号"), ERR_UNKNOW("999",
			"ERR_UNKNOW", "未知错误");

	public static String getText(String code) {
		ErrorCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ErrorCode type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	public static String getChText(String code) {
		ErrorCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ErrorCode type = p[i];
			if (type.getCode().equals(code))
				return type.getChText();
		}
		return null;
	}

	private String code;

	private String text;

	private String chText;

	public String getChText() {
		return chText;
	}

	private ErrorCode(String code, String text, String chText) {
		this.code = code;
		this.text = text;
		this.chText = chText;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

}
