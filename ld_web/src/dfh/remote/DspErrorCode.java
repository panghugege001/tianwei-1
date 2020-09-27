package dfh.remote;

public enum DspErrorCode {
	SUCCESS("0","成功"),
	KEY_ERROR("key_error","key不正确"),
	NETWORK_ERROR("network_error","网络传输不稳定，请稍后再试"),
	ACCOUNT_ADD_FAIL("account_add_fail","添加账户失败,密码不正确或账户已经存在"),
	ACCOUNT_NOT_EXSIT("account_not_exsit","帐号不存在"),
	NOT_ENOUGH_CREDIT("not_enough_credit","ap平台额度不足"),
	ERROR("error","系统繁忙，请联系在线客服");

	public static String getText(String code) {
		DspErrorCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			DspErrorCode type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	

	private String code;

	private String text;	

	private DspErrorCode(String code, String text) {
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
