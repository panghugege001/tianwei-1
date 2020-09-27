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
@Table(name = "quotalrevision", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class QuotalRevision implements java.io.Serializable {

	// Fields

    private Integer billno;
	private String loginname;
	private Timestamp createtime;
	private String type;
	private Double credit;
	private Double newCredit;
	private Double remit;
	private String remark; 
	private String ip;
	private String operator;
	private Integer examine;		//0 未审核  1审核通过 2 审核不通过
	private Timestamp examinetime;
	private String examineRemark;
	private String examineIp;
	private String examineOperator;
	

	// Constructors

	/** default constructor */
	public QuotalRevision() {
	}

	/** minimal constructor */
	public QuotalRevision(Integer billno, String loginname,Timestamp createtime, String type, Double credit, Double newCredit, Double remit,String remark,String ip, String operator,Integer examine, Timestamp examinetime, String examineRemark, String examineIp, String examineOperator) {
		this.billno = billno;
		this.loginname = loginname;
		this.createtime = createtime;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
		this.remark = remark; 
		this.ip = ip;
		this.operator = operator;
		this.examine = examine;		//0 未审核  1审核通过 2 审核不通过
		this.examinetime = examinetime;
		this.examineRemark = examineRemark;
		this.examineIp = examineIp;
		this.examineOperator = examineOperator;
	}

	/** full constructor */
	public QuotalRevision(Integer billno, String loginname, Timestamp createtime, String type, Double credit, Double newCredit, Double remit, String remark, Integer examine) {
		this.billno = billno;
		this.loginname = loginname;
		this.createtime = createtime;
		this.type = type;
		this.credit = credit;
		this.newCredit = newCredit;
		this.remit = remit;
		this.remark = remark;
		this.examine = examine;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "billno")
    public Integer getBillno() {
        return this.billno;
    }
    
    public void setBillno(Integer billno) {
        this.billno = billno;
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

	@javax.persistence.Column(name = "examine")
	public Integer getExamine() {
		return examine;
	}

	public void setExamine(Integer examine) {
		this.examine = examine;
	}
	
	@javax.persistence.Column(name = "examineRemark")
	public String getExamineRemark() {
		return examineRemark;
	}

	public void setExamineRemark(String examineRemark) {
		this.examineRemark = examineRemark;
	}

	@javax.persistence.Column(name = "examinetime")
	public Timestamp getExaminetime() {
		return examinetime;
	}

	public void setExaminetime(Timestamp examinetime) {
		this.examinetime = examinetime;
	}

	@javax.persistence.Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@javax.persistence.Column(name = "operator")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@javax.persistence.Column(name = "examineIp")
	public String getExamineIp() {
		return examineIp;
	}

	public void setExamineIp(String examineIp) {
		this.examineIp = examineIp;
	}

	@javax.persistence.Column(name = "examineOperator")
	public String getExamineOperator() {
		return examineOperator;
	}

	public void setExamineOperator(String examineOperator) {
		this.examineOperator = examineOperator;
	}

}