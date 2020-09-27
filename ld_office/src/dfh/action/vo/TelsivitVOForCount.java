package dfh.action.vo;

import java.io.Serializable;
import java.util.Date;

public class TelsivitVOForCount implements Serializable{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4417415456675375461L;
	
	private int taskstatus,sum,fail,success,id;
	private String operator,taskname;
	private Date createtime;
	
	
	public TelsivitVOForCount(){
		
	}

	public TelsivitVOForCount(int id, String taskname,int taskstatus,Date createtime, int sum,
			int fail, int success, String operator) {
		super();
		this.id = id;
		this.taskstatus = taskstatus;
		this.sum = sum;
		this.fail = fail;
		this.success = success;
		this.operator = operator;
		this.taskname = taskname;
		this.createtime = createtime;
	}

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(int taskstatus) {
		this.taskstatus = taskstatus;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getFail() {
		return fail;
	}
	public void setFail(int fail) {
		this.fail = fail;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
