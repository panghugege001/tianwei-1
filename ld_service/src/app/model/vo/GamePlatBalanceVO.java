package app.model.vo;

public class GamePlatBalanceVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 2268248098619331452L;
	
	private String loginname;//账号
	private String password;//密码
	private String plat;//平台
	private String balance;//余额
	private String msg;//错误原因
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	}