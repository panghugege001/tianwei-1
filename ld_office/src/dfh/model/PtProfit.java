package dfh.model;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ptprofit", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtProfit implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344765238544828494L;
	private String uuid;
	private Integer userId;
	private Double amount;
	private Double betCredit;
	private Date starttime;
	private Date endtime;
	private String loginname;
	private Double payOut;


	/** default constructor */
	public PtProfit() {
	}

	@Id
	@javax.persistence.Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	@javax.persistence.Column(name = "starttime")
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	@javax.persistence.Column(name = "endtime")
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@Override
	public String toString() {
		return "PtProfit [uuid=" + uuid + ", userId=" + userId + ", amount=" + amount + ", betCredit=" + betCredit + ", starttime=" + starttime
				+ ", endtime=" + endtime + ", loginname=" + loginname + ", payOut=" + payOut + "]";
	}
	
}