package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "guestbook", catalog = "longdu_email")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Guestbook implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer referenceId;
	private Integer flag;
	private Integer rcount;
	private String ipaddress;
	private String adminname;
	private String username;
	private String email;
	private String phone;
	private String qq;
	private Timestamp createdate;
	private String title;
	private String content;
	private Integer adminstatus;
	private Integer userstatus;
	private String updateid;
	private Integer isadmin;
	private Integer message;

	// Constructors

	/** default constructor */
	public Guestbook() {
	}

	/** minimal constructor */
	public Guestbook(Integer flag, String ipaddress, Timestamp createdate,
			String title, String content) {
		this.flag = flag;
		this.ipaddress = ipaddress;
		this.createdate = createdate;
		this.title = title;
		this.content = content;
	}

	/** full constructor */
	public Guestbook(Integer referenceId, Integer flag, Integer rcount,
			String ipaddress, String username, String email, String phone,
			String qq, Timestamp createdate, String title, String content) {
		this.referenceId = referenceId;
		this.flag = flag;
		this.rcount = rcount;
		this.ipaddress = ipaddress;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.qq = qq;
		this.createdate = createdate;
		this.title = title;
		this.content = content;
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

	@javax.persistence.Column(name = "referenceId")
	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "rcount")
	public Integer getRcount() {
		return this.rcount;
	}

	public void setRcount(Integer rcount) {
		this.rcount = rcount;
	}

	@javax.persistence.Column(name = "ipaddress")
	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	@javax.persistence.Column(name = "username")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@javax.persistence.Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@javax.persistence.Column(name = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@javax.persistence.Column(name = "qq")
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@javax.persistence.Column(name = "createdate")
	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@javax.persistence.Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@javax.persistence.Column(name = "adminname")
	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	@javax.persistence.Column(name = "updateid")
	public String getUpdateid() {
		return updateid;
	}

	public void setUpdateid(String updateid) {
		this.updateid = updateid;
	}

	@javax.persistence.Column(name = "adminstatus")
	public Integer getAdminstatus() {
		return adminstatus;
	}

	public void setAdminstatus(Integer adminstatus) {
		this.adminstatus = adminstatus;
	}

	@javax.persistence.Column(name = "userstatus")
	public Integer getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(Integer userstatus) {
		this.userstatus = userstatus;
	}

	@javax.persistence.Column(name = "isadmin")
	public Integer getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(Integer isadmin) {
		this.isadmin = isadmin;
	}

	@javax.persistence.Column(name = "message")
	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}
	
	
	
}