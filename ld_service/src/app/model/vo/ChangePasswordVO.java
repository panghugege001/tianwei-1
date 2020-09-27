package app.model.vo;


public class ChangePasswordVO implements java.io.Serializable {
	private static final long serialVersionUID = -3139906349238149959L;
	private String loginname;//登录名
	private String 	originalPassword;//旧密码
	private String 	newPassword;//新密码
	private String 	ip;//新密码
	private String 	msg;//
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getOriginalPassword() {
		return originalPassword;
	}
	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}