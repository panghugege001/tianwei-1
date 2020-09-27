package dfh.model.enums;

/**
 * NT游戏平台接口返回错误码
 * @author chaoren
 */
public enum NTErrorCode {
	/* 注释中文内容为接口返回状态表示的意思 */
	ONE("1","参数错误"), //参数错误
	TWO("2","货币配置有误"), //货币错误
	THREE("3","令牌信息有误"),  //token或secret key 有误
	FOUR("4","所属组有误"), //group id错误
	FIVE("5","尚未激活"), //会员id错误
	SIX("6","用户无法进入游戏"), //用户名不能进入游戏
	SEVEN("7","资金不足"), //资金不足
	EIGHT("8","日期错误"),
	NINE("9","NT服务器返回异常"); //日期错误
	
	private String code;
	private String msg;
	
	private NTErrorCode(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	public static String compare(String code){
		String msg = "";
		for (NTErrorCode nc : NTErrorCode.values()) {
			if (nc.code.equals(code)){
				msg = nc.msg;
				break;
			}
		}
		return msg;
	}
}
