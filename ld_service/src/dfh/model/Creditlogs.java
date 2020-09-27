package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Creditlogs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditlogs", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Creditlogs implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private Date createtime;
	private String type;
	private Double credit;
	private Double newCredit;
	private Double remit;
	private String remark;

	private String tempCreateTime;
	// Constructors

	/** default constructor */
	public Creditlogs() {
	}

	/** minimal constructor */
	public Creditlogs(String loginname, String type, Double credit, Double newCredit, Double remit) {
		this.loginname = loginname;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
	}

	/** full constructor */
	public Creditlogs(String loginname, Timestamp createtime, String type, Double credit, Double newCredit, Double remit, String remark) {
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

	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
		
		if (this.createtime != null) {

			tempCreateTime = dfh.utils.DateUtil
					.formatDateForStandard(this.createtime);
		}
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
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

}