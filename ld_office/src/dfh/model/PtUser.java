package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ptuser", catalog = "longdu_email")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtUser implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6317203044544800971L;
	private Integer id;
	private Integer userId;
	private Integer userStat;
	private Timestamp logintime;
	private String loginname;
	
	public PtUser() {
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

	@javax.persistence.Column(name = "userId")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@javax.persistence.Column(name = "userStat")
	public Integer getUserStat() {
		return userStat;
	}

	public void setUserStat(Integer userStat) {
		this.userStat = userStat;
	}
	
	@javax.persistence.Column(name = "loginTime")
	public Timestamp getLogintime() {
		return logintime;
	}

	public void setLogintime(Timestamp logintime) {
		this.logintime = logintime;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
}