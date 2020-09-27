package app.model.vo;


public class TransferVO implements java.io.Serializable {
	private static final long serialVersionUID = -3139906349238679959L;
	private String  loginName;//用户账号
	private String  from;//源账户（出）
	private String 	to;//目标账户（进）
	private String 	money;//转账金额
	private String 	code;//错误编码
	private String 	msg;//错误信息
	private String 	sid;//
	
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}