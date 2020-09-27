package dfh.model.enums;

public enum WeCustomerErrEnum {
	C00000("00000", "接口调用正常！"), C00001("00001", "用户名为空！"), C00002("00002","用户不存在！"), C00003("00003", "系统异常！"), C00004("00004", "加密错误！");

	private String code;
	private String msg;

	private WeCustomerErrEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
