package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Autotask entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "autotask", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Autotask implements java.io.Serializable {

	// Fields

	private Integer id;
	private String taskType;
	private Integer totalRecords;
	private Integer failRecords;
	private Integer finishRecords;
	private Timestamp refreshTime;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer flag;
	private String remark;
	private String operator;

	// Constructors

	/** default constructor */
	public Autotask() {
	}

	/** minimal constructor */
	public Autotask(String taskType, Integer totalRecords, Integer failRecords, Integer finishRecords, Timestamp startTime, Integer flag, String operator) {
		this.taskType = taskType;
		this.totalRecords = totalRecords;
		this.failRecords = failRecords;
		this.finishRecords = finishRecords;
		this.startTime = startTime;
		this.flag = flag;
		this.operator = operator;
	}

	/** full constructor */
	public Autotask(String taskType, Integer totalRecords, Integer failRecords, Integer finishRecords, Timestamp refreshTime, Timestamp startTime, Timestamp endTime, Integer flag, String remark,
			String operator) {
		this.taskType = taskType;
		this.totalRecords = totalRecords;
		this.failRecords = failRecords;
		this.finishRecords = finishRecords;
		this.refreshTime = refreshTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.flag = flag;
		this.remark = remark;
		this.operator = operator;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "taskType")
	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@javax.persistence.Column(name = "totalRecords")
	public Integer getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	@javax.persistence.Column(name = "failRecords")
	public Integer getFailRecords() {
		return this.failRecords;
	}

	public void setFailRecords(Integer failRecords) {
		this.failRecords = failRecords;
	}

	@javax.persistence.Column(name = "finishRecords")
	public Integer getFinishRecords() {
		return this.finishRecords;
	}

	public void setFinishRecords(Integer finishRecords) {
		this.finishRecords = finishRecords;
	}

	@javax.persistence.Column(name = "refreshTime")
	public Timestamp getRefreshTime() {
		return this.refreshTime;
	}

	public void setRefreshTime(Timestamp refreshTime) {
		this.refreshTime = refreshTime;
	}

	@javax.persistence.Column(name = "startTime")
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@javax.persistence.Column(name = "endTime")
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "operator")
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}