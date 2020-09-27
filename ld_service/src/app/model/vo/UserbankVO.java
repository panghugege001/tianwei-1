package app.model.vo;
public class UserbankVO {

	private Integer id;
	private String loginname;
	private String bankno;
	private String bankname;
	private String bankaddress;
	private String addtime;
	private Integer flag;

	private String password;
	private String message;
	private String familyName;
	
	public String getFamilyName() {
		return familyName;
	}


	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}


	public UserbankVO() {
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public String getBankno() {
		return bankno;
	}


	public void setBankno(String bankno) {
		this.bankno = bankno;
	}


	public String getBankname() {
		return bankname;
	}


	public void setBankname(String bankname) {
		this.bankname = bankname;
	}


	public String getBankaddress() {
		return bankaddress;
	}


	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}


	public String getAddtime() {
		return addtime;
	}


	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}


	public Integer getFlag() {
		return flag;
	}


	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	
}