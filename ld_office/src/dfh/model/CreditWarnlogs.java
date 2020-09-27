package dfh.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Creditlogs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "credit_warnlogs", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CreditWarnlogs implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private Timestamp createtime;
	private String type;
	private Double credit;
	private Double newCredit;
	private Double remit;
	private String remark;

	// Constructors

	/** default constructor */
	public CreditWarnlogs() {
	}

	/** minimal constructor */
	public CreditWarnlogs(String loginname, String type, Double credit, Double newCredit, Double remit) {
		this.loginname = loginname;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
	}

	/** full constructor */
	public CreditWarnlogs(String loginname, Timestamp createtime, String type, Double credit, Double newCredit, Double remit, String remark) {
		this.loginname = loginname;
		this.createtime = createtime;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
		this.remark = remark;
	}

	// Property accessors
	@Id
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

	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "credit")
	public Double getCredit() {
		return this.credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	@javax.persistence.Column(name = "newCredit")
	public Double getNewCredit() {
		return this.newCredit;
	}

	public void setNewCredit(Double newCredit) {
		this.newCredit = newCredit;
	}

	@javax.persistence.Column(name = "remit")
	public Double getRemit() {
		return this.remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}