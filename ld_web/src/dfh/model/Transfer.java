package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Transfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="transfer",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Transfer  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String source;
     private String target;
     private Double remit;
     private String loginname;
     private Date createtime;
     private Double credit;
     private Double newCredit;
     private Integer flag;
     private String paymentid;
     private String remark;

     private String tempCreateTime;

    // Constructors
    @Transient
    public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

	/** default constructor */
    public Transfer() {
    }

	/** minimal constructor */
    public Transfer(Long id, String source, String target, Double remit, String loginname, Date createtime, Double credit, Double newCredit, Integer flag) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.remit = remit;
        this.loginname = loginname;
        this.createtime = createtime;
        this.credit = credit;
        this.newCredit = newCredit;
        this.flag = flag;
    }
    
    /** full constructor */
    public Transfer(Long id, String source, String target, Double remit, String loginname, Date createtime, Double credit, Double newCredit, Integer flag, String paymentid, String remark) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.remit = remit;
        this.loginname = loginname;
        this.createtime = createtime;
        this.credit = credit;
        this.newCredit = newCredit;
        this.flag = flag;
        this.paymentid = paymentid;
        this.remark = remark;
    }

   
    // Property accessors
    @Id 
    
@javax.persistence.Column(name = "id")

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
@javax.persistence.Column(name = "source")

    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
@javax.persistence.Column(name = "target")

    public String getTarget() {
        return this.target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
@javax.persistence.Column(name = "remit")

    public Double getRemit() {
        return this.remit;
    }
    
    public void setRemit(Double remit) {
        this.remit = remit;
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
    
@javax.persistence.Column(name = "flag")

    public Integer getFlag() {
        return this.flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
@javax.persistence.Column(name = "paymentid")

    public String getPaymentid() {
        return this.paymentid;
    }
    
    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }
    
@javax.persistence.Column(name = "remark")

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
   








}