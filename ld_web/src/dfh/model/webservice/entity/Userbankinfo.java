package dfh.model.webservice.entity;

public class Userbankinfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String loginname;
	private String bankno;
	private String bankname;
	private String bankaddress;
//	private Timestamp addtime;
	private String addtime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public Userbankinfo() {
	}

	/** full constructor */
	public Userbankinfo(String loginname, String bankno, String bankname,
			String bankaddress, String addtime, Integer flag) {
		this.loginname = loginname;
		this.bankno = bankno;
		this.bankname = bankname;
		this.bankaddress = bankaddress;
		this.addtime = addtime;
		this.flag = flag;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getBankno() {
		return this.bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankaddress() {
		return this.bankaddress;
	}

	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}