package dfh.model.common;

public enum ResponseEnum {
    SUCCESS("0000", "成功"),
	SC_10000("10000", "成功"),
	SC_10001("10001", "系统异常"),
    PARAMETER_MISSING("0100", "必填参数不能为空"),
    PARAMETER_WRONG("0101", "参数有误"),
    LOGIN_OVER("0201", "[提示]你的登录已过期，请从首页重新登录"),
	AGENT_02("0202", "[提示]代理不能使用在线支付！"),
	AGENT_03("0203", "[提示]代理不能执行积分相关操作！"),
	SC_10002("10002", "失败"),
    SERVER_ERROR("0200", "服务器抛出异常");

    private String code;
    private String desc;

    private ResponseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
