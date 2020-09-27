package app.model.vo;

import dfh.model.Users;

public class LoginInfoVO implements java.io.Serializable{
	private static final long serialVersionUID = -3132726349008147359L;
	private String code;
	private String msg;
	private Users user;
	
	public LoginInfoVO() {
		super();
	}
	public LoginInfoVO(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
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
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}
