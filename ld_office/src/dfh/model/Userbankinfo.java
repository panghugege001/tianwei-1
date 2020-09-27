package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Userbankinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "userbankinfo", catalog = "tianwei")
public class Userbankinfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private String bankno;
	private String bankname;
	private Timestamp addtime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public Userbankinfo() {
	}

	/** full constructor */
	public Userbankinfo(String loginname, String bankno, String bankname,
			Timestamp addtime, Integer flag) {
		this.loginname = loginname;
		this.bankno = bankno;
		this.bankname = bankname;
		this.addtime = addtime;
		this.flag = flag;
	}

	public Userbankinfo(String loginname, String bankname, Integer flag) {
		super();
		this.loginname = loginname;
		this.bankname = bankname;
		this.flag = flag;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "loginname", nullable = false, length = 30)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "bankno", nullable = false, length = 50)
	public String getBankno() {
		return this.bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	@Column(name = "bankname", nullable = false, length = 50)
	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	@Column(name = "addtime", nullable = false, length = 19)
	public Timestamp getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	@Column(name = "flag", nullable = false)
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}