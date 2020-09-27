package dfh.model.enums;

public enum ResponseEnum {
    error1("1001", "未知错误 "),
    error2("1002", "转账模式错误 "),
    error3("1003", "卡信息有误 "),
    error4("1004", "不支持的银行 "),
    error5("1005", "卡号非数字 "),
    error6("1006", "卡省份城市错误 "),
    error7("1007", "卡号已存在 "),
    error8("1008", "卡登录名已存在"),
    error9("1009", "卡号不存在 "),
    error10("1010", "没有收款卡 "),
    error11("1011", "金额格式错误，非数字或则小于0 "),
    error12("1012", "订单号长度大于24 "),
    error13("1013", "订单号已存在 "),
    error14("1014", "订单不存在 "),
    error15("1015", "没有权限 "),
    error16("1016", "登录超时 "),
    error17("1017", "密码错误超过3次 "),
    error19("1019", "数据格式错误，非json格式 "),
    error38("1038", "IP地址不在白名单 "),
    error36("1036", "签名验证失败 ");
    
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
	
	public static String getText(String code) {
		ResponseEnum[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ResponseEnum type = p[i];
			if (type.getCode().equals(code))
				return type.getDesc();
		}
		return null;
	}
    
}
