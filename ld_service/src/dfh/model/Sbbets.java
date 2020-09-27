package dfh.model;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sb_bets", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Sbbets implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344765238544828494L;
	private Integer id;
	private String loginname;
	private Date createtime;
	private Double sbbets;
	private String sbinfo;
	private String sbremark;
	
	
	/** default constructor */
	public Sbbets() {
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

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "sbbets")
	public Double getSbbets() {
		return sbbets;
	}

	public void setSbbets(Double sbbets) {
		this.sbbets = sbbets;
	}

	@javax.persistence.Column(name = "sbinfo")
	public String getSbinfo() {
		return sbinfo;
	}

	public void setSbinfo(String sbinfo) {
		this.sbinfo = sbinfo;
	}

	@javax.persistence.Column(name = "sbremark")
	public String getSbremark() {
		return sbremark;
	}

	public void setSbremark(String sbremark) {
		this.sbremark = sbremark;
	}
	
}