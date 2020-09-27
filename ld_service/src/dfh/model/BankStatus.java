package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 银行状态
 */
@Entity
@Table(name = "t_bank_status", catalog = "tianwei")
public class BankStatus implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3507119196881617751L;
	
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 银行名称
	 */
	private String bankname;
	/**
	 * 状态: "MAINTENANCE" 维护中 "OK" 正常
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "bankname")
	public String getBankname() {
		return bankname;
	}
	
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	
	@javax.persistence.Column(name = "status")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@javax.persistence.Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return "BankStatus [id=" + id + ", bankname=" + bankname + ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
	
}
