package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GPI token
 *
 */

@Entity
@Table(name = "gpitoken", catalog = "tianwei")
public class GPIToken implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String loginname;
	private String token;
	private Date updateTime;
	private Date createTime;
	private Integer isTest;
	
	public GPIToken() {
	}
	
	@Id
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	@javax.persistence.Column(name = "token")
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@javax.persistence.Column(name = "updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "isTest")
	public Integer getIsTest() {
		return isTest;
	}
	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

}
