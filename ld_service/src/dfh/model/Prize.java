package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Prize entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "prize", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Prize implements java.io.Serializable {

	// Fields
	private String pno;
	private String title;
	private String loginname;
	private Double tryCredit;
	private String remark;

	// Constructors

	/** default constructor */
	public Prize() {
	}

	/** minimal constructor */
	public Prize(String pno, String title, String loginname, Double tryCredit, String remark) {
		this.pno = pno;
		this.title = title;
		this.loginname = loginname;
		this.tryCredit = tryCredit;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "pno")
	public String getPno() {
		return this.pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "tryCredit")
	public Double getTryCredit() {
		return this.tryCredit;
	}

	public void setTryCredit(Double tryCredit) {
		this.tryCredit = tryCredit;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}