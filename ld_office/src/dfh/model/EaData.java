package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ea_data", catalog = "gamedata")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class EaData implements java.io.Serializable {

	// Fields

	private String uuid;
	private Integer pm;
	private String loginname;
	private Double ztze;
	private Integer tzcs;
	private Double pjtze;
	private Double zsy;
	private Date starttime;
	private Date endtime;
	private Date date;


	// Constructors

	/** default constructor */
	public EaData() {
	}


	@Id
	@javax.persistence.Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	@javax.persistence.Column(name = "pm")
	public Integer getPm() {
		return pm;
	}


	public void setPm(Integer pm) {
		this.pm = pm;
	}


	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "ztze")
	public Double getZtze() {
		return ztze;
	}


	public void setZtze(Double ztze) {
		this.ztze = ztze;
	}

	@javax.persistence.Column(name = "tzcs")
	public Integer getTzcs() {
		return tzcs;
	}


	public void setTzcs(Integer tzcs) {
		this.tzcs = tzcs;
	}

	@javax.persistence.Column(name = "pjtze")
	public Double getPjtze() {
		return pjtze;
	}


	public void setPjtze(Double pjtze) {
		this.pjtze = pjtze;
	}

	@javax.persistence.Column(name = "zsy")
	public Double getZsy() {
		return zsy;
	}


	public void setZsy(Double zsy) {
		this.zsy = zsy;
	}

	@javax.persistence.Column(name = "starttime")
	public Date getStarttime() {
		return starttime;
	}


	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	@javax.persistence.Column(name = "endtime")
	public Date getEndtime() {
		return endtime;
	}


	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@javax.persistence.Column(name = "date")
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
}