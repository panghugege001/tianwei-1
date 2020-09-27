package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activity", catalog = "tianwei")
public class Activity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9079715067179381177L;
	public Integer id;
	public String activityName;
	public Date activityStart;
	public Date activityEnd;
	public Date backstageStart;
	public Date backstageEnd;
	public Date createDate;
	public Double activityPercent;
	public Integer activityStatus;
	public String userrole;
	private String remark;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "activityName")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Column(name = "activityStart")
	public Date getActivityStart() {
		return activityStart;
	}

	public void setActivityStart(Date activityStart) {
		this.activityStart = activityStart;
	}

	@Column(name = "activityEnd")
	public Date getActivityEnd() {
		return activityEnd;
	}

	public void setActivityEnd(Date activityEnd) {
		this.activityEnd = activityEnd;
	}

	@Column(name = "activityStatus")
	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	@Column(name = "activityPercent")
	public Double getActivityPercent() {
		return activityPercent;
	}

	public void setActivityPercent(Double activityPercent) {
		this.activityPercent = activityPercent;
	}

	@Column(name = "createDate")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "userrole")
	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	@Column(name = "backstageStart")
	public Date getBackstageStart() {
		return backstageStart;
	}

	public void setBackstageStart(Date backstageStart) {
		this.backstageStart = backstageStart;
	}

	@Column(name = "backstageEnd")
	public Date getBackstageEnd() {
		return backstageEnd;
	}

	public void setBackstageEnd(Date backstageEnd) {
		this.backstageEnd = backstageEnd;
	}
	
	

}
