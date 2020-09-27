package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Telvisitremark entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "telvisitremark", catalog = "tianwei")
public class Telvisitremark implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer telvisitid;
	private Timestamp addtime;
	private String remark;
	private String operator;
	private Integer execstatus;

	// Constructors

	/** default constructor */
	public Telvisitremark() {
	}

	/** minimal constructor */
	public Telvisitremark(Integer telvisitid, Timestamp addtime) {
		this.telvisitid = telvisitid;
		this.addtime = addtime;
	}

	/** full constructor */
	public Telvisitremark(Integer telvisitid, Timestamp addtime, String remark,String operator,Integer execstatus) {
		this.telvisitid = telvisitid;
		this.addtime = addtime;
		this.remark = remark;
		this.operator=operator;
		this.execstatus=execstatus;
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

	@Column(name = "telvisitid", nullable = false)
	public Integer getTelvisitid() {
		return this.telvisitid;
	}

	public void setTelvisitid(Integer telvisitid) {
		this.telvisitid = telvisitid;
	}

	@Column(name = "addtime", nullable = false, length = 19)
	public Timestamp getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	@Column(name = "remark", length = 2000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "operator", length = 30)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(name = "execstatus", nullable = false)
	public Integer getExecstatus() {
		return this.execstatus;
	}

	public void setExecstatus(Integer execstatus) {
		this.execstatus = execstatus;
	}
}