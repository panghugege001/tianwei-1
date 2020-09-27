package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "platform_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PlatformData implements java.io.Serializable {

	// Fields

	private String uuid;
	private String platform;
	private String loginname;
	private Double bet;
	private Double profit;
	private Date starttime;
	private Date endtime;
	private Date updatetime;


	// Constructors

	/** default constructor */
	public PlatformData() {
	}


	@Id
	@javax.persistence.Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}


	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "bet")
	public Double getBet() {
		return bet;
	}


	public void setBet(Double bet) {
		this.bet = bet;
	}


	@javax.persistence.Column(name = "profit")
	public Double getProfit() {
		return profit;
	}


	public void setProfit(Double profit) {
		this.profit = profit;
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

	@javax.persistence.Column(name = "updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
}