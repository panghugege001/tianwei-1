package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Operator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sendemail", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Sendemail implements java.io.Serializable {

	private Integer id ;
	private String email;
	private String username;
	private String emailflag;//邮件地址是否有效 0无效 1有效 2未检测 3未知
	private String sendflag;//邮件是否发送 0未发送 1待发送 2已发送 3发送失败
	private String isopen;
	private String bt;
	private String nr;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="emailflag")
	public String getEmailflag() {
		return emailflag;
	}
	public void setEmailflag(String emailflag) {
		this.emailflag = emailflag;
	}
	
	@Column(name="sendflag")
	public String getSendflag() {
		return sendflag;
	}
	public void setSendflag(String sendflag) {
		this.sendflag = sendflag;
	}
	@Column(name="isopen")
	public String getIsopen() {
		return isopen;
	}
	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}
	@Column(name="bt")
	public String getBt() {
		return bt;
	}
	public void setBt(String bt) {
		this.bt = bt;
	}
	@Column(name="nr")
	public String getNr() {
		return nr;
	}
	public void setNr(String nr) {
		this.nr = nr;
	}
	

}