package app.model.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(UserSidNameKey.class)
@Table(name = "user_sid_name", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = false, dynamicInsert = false)
public class UserSidName implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860003002132642285L;

	@Id
	private String loginname;
	
	@Id
	private String sid;
	
	@Column(name="os")
	private String os;
	
	@Column(name="osversion")
	private String osVersion;
	
	@Column(name="mobilemodel")
	private String mobileModel;
	
	public UserSidName() {
		super();
	}
	public UserSidName(String loginname, String sid, String os, String osVersion, String mobileModel) {
		super();
		this.loginname = loginname;
		this.sid = sid;
		this.os = os;
		this.osVersion = osVersion;
		this.mobileModel = mobileModel;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getMobileModel() {
		return mobileModel;
	}
	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}
}
