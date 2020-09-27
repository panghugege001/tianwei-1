package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Actionlogs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "actionlogs", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Actionlogs implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private String action;
	private Timestamp createtime;
	private String remark;

	// Constructors

	/** default constructor */
	public Actionlogs() {
	}

	/** minimal constructor */
	public Actionlogs(String loginname) {
		this.loginname = loginname;
	}

	/** full constructor */
	public Actionlogs(String loginname, String action, Timestamp createtime, String remark) {
		this.loginname = loginname;
		this.action = action;
		this.createtime = createtime;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "action")
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}