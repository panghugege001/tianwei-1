package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "other_customer", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Customer {

	private Integer id;
	private String name;
	private String email;
	private String phone;
	private Integer isreg;
	private Integer isdeposit;
	private Integer phonestatus;
	private Integer userstatus;
	private String cs;
	private String remark;
	private Date createTime;
	private Integer batch;
	private String shippingCode;
	private String type ;
	private Date noticeTime;  //邮件通知时间
	private String qq;
	
	private String emailflag;
	
	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Customer() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@javax.persistence.Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@javax.persistence.Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@javax.persistence.Column(name = "isreg")
	public Integer getIsreg() {
		return isreg;
	}

	public void setIsreg(Integer isreg) {
		this.isreg = isreg;
	}

	@javax.persistence.Column(name = "isdeposit")
	public Integer getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(Integer isdeposit) {
		this.isdeposit = isdeposit;
	}

	@javax.persistence.Column(name = "phonestatus")
	public Integer getPhonestatus() {
		return phonestatus;
	}

	public void setPhonestatus(Integer phonestatus) {
		this.phonestatus = phonestatus;
	}

	@javax.persistence.Column(name = "userstatus")
	public Integer getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(Integer userstatus) {
		this.userstatus = userstatus;
	}

	@javax.persistence.Column(name = "cs")
	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "batch")
	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	
	@javax.persistence.Column(name = "shippingcode")
	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	@javax.persistence.Column(name = "noticeTime")
	public Date getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	@javax.persistence.Column(name = "qq")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@javax.persistence.Column(name = "emailflag")
	public String getEmailflag() {
		return emailflag;
	}

	public void setEmailflag(String emailflag) {
		this.emailflag = emailflag;
	}
	
	
	
}
