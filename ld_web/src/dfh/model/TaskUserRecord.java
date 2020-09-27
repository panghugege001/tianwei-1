package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "task_user_record", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TaskUserRecord implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String title;
	private Integer taskId;
	private Integer type;
	private Double historyBet;
	private Integer isAdd;
	private Date createtime;
	private Date updatetime;
	private String tmpCreatetime;

	public TaskUserRecord() {
		super();
	}


	public TaskUserRecord(String username, String title, Integer taskId, Integer type, Double historyBet,
			Integer isAdd, Date createtime, Date updatetime) {
		super();
		this.username = username;
		this.title = title;
		this.taskId = taskId;
		this.type = type;
		this.historyBet = historyBet;
		this.isAdd = isAdd;
		this.createtime = createtime;
		this.updatetime = updatetime;
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


	public Integer getTaskId() {
		return taskId;
	}


	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Double getHistoryBet() {
		return historyBet;
	}


	public void setHistoryBet(Double historyBet) {
		this.historyBet = historyBet;
	}


	public Integer getIsAdd() {
		return isAdd;
	}


	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Transient
	public String getTmpCreatetime() {
		return tmpCreatetime;
	}

	public void setTmpCreatetime(String tmpCreatetime) {
		this.tmpCreatetime = tmpCreatetime;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


}