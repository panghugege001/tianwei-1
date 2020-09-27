package dfh.action.vo;
/**
 * 登录vo
 * @author Administrator
 *
 */
public class MsLoginInfoVo {
	private String username;
	private String userAccount;
	private String password;
	
	public MsLoginInfoVo(String username, String userAccount, String password) {
		this.username = username;
		this.userAccount = userAccount;
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
}
