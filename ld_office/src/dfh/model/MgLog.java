package dfh.model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * mglog entity.
 */
@Entity
@Table(name = "mglog",catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MgLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String loginname;
	private String actionType;
	private String seq;
	private String gameid;
	private String actionId;
	private Date actiontime;
	private Integer amount;
	private Integer balance;
	private String exttransactionid;
	private Integer isend;
	private String remark;

	
	/** default constructor */
	public MgLog() {
	}

	@Id
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	
	@javax.persistence.Column(name = "gameid")
	public String getGameid() {
		return gameid;
	}
	
	@javax.persistence.Column(name = "actiontype")
	public String getActionType() {
		return actionType;
	}

	@javax.persistence.Column(name = "seq")
	public String getSeq() {
		return seq;
	}

	@javax.persistence.Column(name = "actionid")
	public String getActionId() {
		return actionId;
	}

	@javax.persistence.Column(name = "actiontime")
	public Date getActiontime() {
		return actiontime;
	}
	@javax.persistence.Column(name = "amount")
	public Integer getAmount() {
		return amount;
	}

	@javax.persistence.Column(name = "balance")
	public Integer getBalance() {
		return balance;
	}

	@javax.persistence.Column(name = "exttransactionid")
	public String getExttransactionid() {
		return exttransactionid;
	}

	@javax.persistence.Column(name = "isend")
	public Integer getIsend() {
		return isend;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public void setSeq(String seq) {
		this.seq = seq;
	}


	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	public void setBalance(Integer balance) {
		this.balance = balance;
	}


	public void setExttransactionid(String exttransactionid) {
		this.exttransactionid = exttransactionid;
	}


	public void setIsend(Integer isend) {
		this.isend = isend;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void setActiontime(Date actiontime) {
		this.actiontime = actiontime;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	
}