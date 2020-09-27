package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "agtrygame", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgTryGame implements java.io.Serializable {

	// Fields

	private String agName;
	private String agPassword;
	private String agPhone;
	private String agIp;
	private Date agRegDate;
	private Integer agIsLogin;

	// Constructors

	/** default constructor */
	public AgTryGame() {
	}

	/** full constructor */
	public AgTryGame(String agName, String agPassword,String agPhone,String agIp,Date agRegDate,Integer agIsLogin) {
		this.agName = agName;
		this.agPassword = agPassword;
		this.agPhone = agPhone;
		this.agIp = agIp;
		this.agRegDate = agRegDate;
		this.agIsLogin = agIsLogin;
	}
	// Property accessors
	@Id
	@javax.persistence.Column(name = "agname")
	public String getAgName() {
		return agName;
	}

	public void setAgName(String agName) {
		this.agName = agName;
	}
	@javax.persistence.Column(name = "agpassword")
	public String getAgPassword() {
		return agPassword;
	}

	public void setAgPassword(String agPassword) {
		this.agPassword = agPassword;
	}
	@javax.persistence.Column(name = "agip")
	public String getAgIp() {
		return agIp;
	}

	public void setAgIp(String agIp) {
		this.agIp = agIp;
	}
	@javax.persistence.Column(name = "agregdate")
	public Date getAgRegDate() {
		return agRegDate;
	}

	public void setAgRegDate(Date agRegDate) {
		this.agRegDate = agRegDate;
	}
	@javax.persistence.Column(name = "agphone")
	public String getAgPhone() {
		return agPhone;
	}

	public void setAgPhone(String agPhone) {
		this.agPhone = agPhone;
	}
	@javax.persistence.Column(name = "agislogin")
	public Integer getAgIsLogin() {
		return agIsLogin;
	}

	public void setAgIsLogin(Integer agIsLogin) {
		this.agIsLogin = agIsLogin;
	}
}