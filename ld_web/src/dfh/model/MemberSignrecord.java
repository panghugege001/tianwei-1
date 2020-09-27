package dfh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MemberSignrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "member_signrecord", catalog = "tianwei")
public class MemberSignrecord implements java.io.Serializable {

	// Fields

	private String loginname;
	private Integer flag;

	// Constructors

	/** default constructor */
	public MemberSignrecord() {
	}

	/** full constructor */
	public MemberSignrecord(String loginname, Integer flag) {
		this.loginname = loginname;
		this.flag = flag;
	}

	// Property accessors
	@Id
	@Column(name = "loginname", unique = true, nullable = false, length = 30)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "flag", nullable = false)
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}