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
 * Actionlogs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "online_token", catalog = "tianwei")
public class OnlineToken implements java.io.Serializable {

	// Fields

	private String loginname;
	private String token;
	private Date createtime;
	private Integer isused;
	private String tempCreatetime ;

	

	@Id
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getIsused() {
		return isused;
	}

	public void setIsused(Integer isused) {
		this.isused = isused;
	}
	@Transient
	public String getTempCreatetime() {
		return tempCreatetime;
	}

	public void setTempCreatetime(String tempCreatetime) {
		this.tempCreatetime = tempCreatetime;
	}

	

}