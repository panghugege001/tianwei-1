package dfh.model; 

import java.util.Date; 

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_preferential", catalog = "tianwei")

@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AppPreferential implements java.io.Serializable {

	// Fields

	@Override
	public String toString() {
		return "AppPreferential [id=" + id + ", loginname=" + loginname + ", transferId=" + transferId + ", version="
				+ version + ", createtime=" + createtime + "]";
	}

	private static final long serialVersionUID = 1L;
	private Long id;
	private String loginname;
	private Long transferId;
	private int version;
	private Date createtime;

	// Constructors

	/** default constructor */
	public AppPreferential() {
	}

	/** minimal constructor */
	public AppPreferential(Long id, String loginname, Long transferId, int version, Date createtime) {
		this.id = id;
		this.loginname = loginname;
		this.transferId = transferId;
		this.version = version;
		this.createtime = createtime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@javax.persistence.Column(name = "id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	}

	@javax.persistence.Column(name = "transferId")
	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	@javax.persistence.Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}