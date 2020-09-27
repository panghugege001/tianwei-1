package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pt_transfer2new_history", catalog="tianwei")
public class PTTransfer2NewHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Double money;
	private Date createTime;
	private String remark;
	
	
	public PTTransfer2NewHistory(){}
	
	public PTTransfer2NewHistory(String username, Date createTime){
		this.username = username;
		this.createTime = createTime;
	}
	
	@Id
	@javax.persistence.Column(name = "username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@javax.persistence.Column(name = "money")
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
