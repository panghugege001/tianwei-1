package dfh.model.enums;

/**
 * 短信网发短信错误编码
 */
public enum DXWErrorCode {
	
	SUCCESS("1","操作成功"),
	ACCOUNT_PATTERN_ERROR("0","帐户格式不正确(正确的格式为:员工编号@企业编号)"),
	SERVER_REJECT("-1","服务器拒绝(速度过快、限时或绑定IP不对等) 如遇速度过快可延时再发"),
	KEY_WRONG("-2","密钥不正确"),
	KEY_LOCK("-3","密钥已锁定"),
	PARAM_WRONG("-4","参数不正确(内容和号码不能为空，手机号码数过多，发送时间错误等)"),
	ACCOUNT_ERROR("-5","无此帐户"),
	ACCOUNT_LOCK_EXPIRE("-6","帐户已锁定或已过期"),
	INTERFACE_NOOPEN("-7","帐户未开启接口发送"),
	CHANNEL_WRONG("-8","不可使用该通道组"),
	NO_MONEY("-9","帐户余额不足"),
	INNER_ERROR("-10","内部错误"),
	FEE_ERROR("-11","扣费失败");
	
	private String code;
	private String msg;
	
	private DXWErrorCode(String code, String msg){
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
		DXWErrorCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			DXWErrorCode type = p[i];
			if (type.getCode().equals(code))
				return type.getMsg();
		}
		return null;
	}
}
