package dfh.model.notdb;

import java.util.Date;

import javax.persistence.Transient;

public class TaskUserRecordVo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private Integer taskId;
	private String title;
	private Integer type;
	private Double historyBet;
	private Integer isAdd;
	private Date createtime;
	private Date updatetime;
	private String tmpCreatetime;
	private Double amount ;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}