package dfh.remote.bean;

public class SportBookLoginValidationBean {

	// ID must start with capital letter “L” followed by no
	// more than 40 digits
	
	private String token;
	
	private String loginName;

	public SportBookLoginValidationBean(String token, String loginName) {
		super();
		this.token = token;
		this.loginName = loginName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@Override
	public String toString() {
		return "SportBookLoginValidationBean [loginName=" + loginName + ", token=" + token + "]";
	}
}
