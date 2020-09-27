package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  
 * @author chaoren
 */
@Entity
@Table(name = "qq_inf_fromnet", catalog = "tianwei")
public class QqInfFromNet implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String loginname; // 用户id
	private String agentname; // 代理id
	private String qq; // 收集的用户qq
	private String email; // 用户email
	private String fromCsQq; // 来自哪个客服qq
	private Date collectTime; // 收集时间
	private Date lastVisitTime; // 最后访问时间
	private Date createTime; // 创建时间
	private Double depositSum; // 存款总数
	private Double betSum; // 投注总数
	private Double win_los; // 输赢总数
	
	@javax.persistence.Column(name = "id")
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@javax.persistence.Column(name = "agentname")
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	@javax.persistence.Column(name = "qq")
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	@javax.persistence.Column(name = "from_cs_qq")
	public String getFromCsQq() {
		return fromCsQq;
	}
	public void setFromCsQq(String fromCsQq) {
		this.fromCsQq = fromCsQq;
	}
	@javax.persistence.Column(name = "collect_time")
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	@javax.persistence.Column(name = "last_visit_time")
	public Date getLastVisitTime() {
		return lastVisitTime;
	}
	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@javax.persistence.Column(name = "deposit_sum")
	public Double getDepositSum() {
		return depositSum;
	}
	public void setDepositSum(Double depositSum) {
		this.depositSum = depositSum;
	}
	@javax.persistence.Column(name = "bet_sum")
	public Double getBetSum() {
		return betSum;
	}
	public void setBetSum(Double betSum) {
		this.betSum = betSum;
	}
	@javax.persistence.Column(name = "win_los")
	public Double getWin_los() {
		return win_los;
	}
	public void setWin_los(Double win_los) {
		this.win_los = win_los;
	}
	@javax.persistence.Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
