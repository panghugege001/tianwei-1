package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ptprofit", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class NTProfit implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id; //UUID
	private Integer userId; //用户ID
	private Double amount; //盈利额=投注额-赔付
	private Double betCredit; //投注额
	private Date startTime; 
	private Date endTime;
	private String loginname; //用户名
	private Double payOut; //赔付
	
	@Id
	@javax.persistence.Column(name = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "userId")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@javax.persistence.Column(name = "betCredit")
	public Double getBetCredit() {
		return betCredit;
	}
	public void setBetCredit(Double betCredit) {
		this.betCredit = betCredit;
	}
	@javax.persistence.Column(name = "startTime")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@javax.persistence.Column(name = "endTime")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@javax.persistence.Column(name = "payOut")
	public Double getPayOut() {
		return payOut;
	}
	public void setPayOut(Double payOut) {
		this.payOut = payOut;
	}
	
}
