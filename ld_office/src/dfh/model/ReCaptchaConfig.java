package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="recaptcha_config",catalog="tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class ReCaptchaConfig  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields    
    private Integer id;
    private String domain;
 	private String site_key;
 	private String secret_key;
 	private String status;
 	private Date createtime;
 	private String remark;
 	
	@Id
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "domain")
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@javax.persistence.Column(name = "site_key")
	public String getSite_key() {
		return site_key;
	}
	public void setSite_key(String site_key) {
		this.site_key = site_key;
	}
	@javax.persistence.Column(name = "secret_key")
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}
	@javax.persistence.Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
 	
}