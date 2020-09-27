package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Creditlogs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bankcreditlogs", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BankCreditlogs implements java.io.Serializable {

	// Fields

	private Integer id;
	private String bankname;
	private Timestamp createtime;
	private String type;
	private Double credit;
	private Double newCredit;
	private Double remit;
	private String remark;

	// Constructors

	/** default constructor */
	public BankCreditlogs() {
	}

	/** minimal constructor */
	public BankCreditlogs(String bankname, String type, Double credit, Double newCredit, Double remit) {
		this.bankname = bankname;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
	}

	/** full constructor */
	public BankCreditlogs(String bankname, Timestamp createtime, String type, Double credit, Double newCredit, Double remit, String remark) {
		this.bankname = bankname;
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
	@javax.persistence.Column(name = "bankname")
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

}