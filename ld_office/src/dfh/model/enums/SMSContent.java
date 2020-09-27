package dfh.model.enums;

/**
 * 短信网发短信模板
 */
public enum SMSContent {
	//$XXX loginname, $MMM 日期, $LEVEL 会员等级, $MONEY 金额
	WITHDRAW("5","尊敬的$XXX会员，您在$MMM申请*提*款*！"),
	MODIFY_PWD("3","尊敬的$XXX会员，您*的*账*号*于$MMM修改了登入密码，请您妥善保护您的登入密码。"),
	ADD_CASH("9","尊敬的$XXX会员，您的*存*款*$MONEY已*添*加,请*您*登*录*查*收!"),
	LEVEL_UP("2","尊敬的$XXX会员，您*的*晋*级*金*$MONEY已*添*加，请您登录查看。");
	
	private String code;
	private String msg;
	
	private SMSContent(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	public static String getText(String code) {
		SMSContent[] p = values();
		for (int i = 0; i < p.length; ++i) {
			SMSContent type = p[i];
			if (type.getCode().equals(code))
				return type.getMsg();
		}
		return null;
	}
}
