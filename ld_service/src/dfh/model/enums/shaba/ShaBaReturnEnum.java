package dfh.model.enums.shaba;


public enum ShaBaReturnEnum {
/*	AuthSessionToken0("auth_session_token","0", "会话令牌有效"), 
	AuthSessionToken1("auth_session_token","1", "无效的密钥或ip不在白名单"), 
	AuthSessionToken2("auth_session_token","2", "供应商数据库中找不到会话令牌"), 
	AuthSessionToken3("auth_session_token","3", "会话令牌无效"),
	
	C00004("check_member_online","0", "会员在线"), 
	C00005("check_member_online","1", "无效的密钥和/或IP地址未列出白名单"), 
	C00006("check_member_online","2", "找不到vendor_member_id"), 
	C00007("check_member_online","3", "会员离线"),*/
	
	ERROR_CODE0(0, "成功"),
	ERROR_CODE1(1, "执行时失败"),
	ERROR_CODE2(2, "用户名重复或用户不存在"),
	ERROR_CODE3(3, "用户不在线"),
	ERROR_CODE4(4, "赔率类型格式错误"),
	ERROR_CODE5(5, "货币格式错误"),
	ERROR_CODE6(6, "供应商ID重复"),
	ERROR_CODE7(7, "最小转入大于最大转入"),
	ERROR_CODE8(8, "[限制提款红色警报]会员达到限制"),
	ERROR_CODE9(9, "供应商ID已废弃"),
	ERROR_CODE10(10, "系统正在维护中");
	
	private Integer code;
	private String message;

	// 构造方法
	private ShaBaReturnEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getmessage() {
		return message;
	}

	public static ShaBaReturnEnum getShaBaReturnEnum(Integer code) {
		
		ShaBaReturnEnum[] enums = values();
		int len = enums.length;
		ShaBaReturnEnum obj = null;
		
		for (int i = 0; i < len; i++) {
			
			ShaBaReturnEnum temp = enums[i];
			
			if (temp.getCode().equals(code)) {
				
				obj = temp;
				break;
			}
		}
		
		return obj;
	}
	
	public static String getmessageValue(Integer code) {
		ShaBaReturnEnum[] enums = values();
		int len = enums.length;
		String message = "";
		
		try {
			for (int i = 0; i < len; i++) {
				ShaBaReturnEnum temp = enums[i];
				if (temp.getCode().equals(code)) {
					message = temp.getmessage();
					break;
				}
			}
		} catch (Exception e) {
		}
		return message;
	}
	
	public static void main(String[] args) {
		System.out.println(ShaBaReturnEnum.getmessageValue(1));
	}
}
