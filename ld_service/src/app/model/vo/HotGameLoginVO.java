package app.model.vo;

public class HotGameLoginVO{
	
	private String platCode;
	private String gameCode;
	private String loginName;
	private String password;
	private String isfun;
	private String gameLoginUrl;
	private String openType;
	private String msg;
	
	public String getIsfun() {
		return isfun;
	}
	public void setIsfun(String isfun) {
		this.isfun = isfun;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlatCode() {
		return platCode;
	}
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getGameLoginUrl() {
		return gameLoginUrl;
	}
	public void setGameLoginUrl(String gameLoginUrl) {
		this.gameLoginUrl = gameLoginUrl;
	}
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
}