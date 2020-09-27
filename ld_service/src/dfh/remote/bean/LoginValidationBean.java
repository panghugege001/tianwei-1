package dfh.remote.bean;

public class LoginValidationBean {

	// ID must start with capital letter “L” followed by no
	// more than 40 digits
	private String id;

	private String userid;

	private String pwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public LoginValidationBean(String id, String userid, String pwd) {
		super();
		this.id = id;
		this.userid = userid;
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return "LoginValidationBean [id=" + id + ", pwd=" + pwd + ", userid=" + userid + "]";
	}

}
