package dfh.model.enums;

/**
 * MG 转账响应枚举
 * @author Administrator
 *
 */
public enum MgRespCode {

	Error(0, "系统异常"),
	Success(1, "成功"),
	accountFrozen(2, "账户已冻结"),
	insufficientCredit(3, "余额不足"),
	signError(4, "签名错误");

	public static String getDesc(int code) {
		MgRespCode p[] = values();
		for (int i = 0; i < p.length; i++) {
			MgRespCode type = p[i]; 
			if (type.getCode().equals(code))
				return type.getDesc();
		}
		return null;
	}
	
	private Integer code;
	private String desc;

	private MgRespCode(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code; 
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
