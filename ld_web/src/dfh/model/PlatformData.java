package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "platform_data", catalog = "tianwei")
public class PlatformData implements java.io.Serializable{

	// Fields

	/** 
	* @fields serialVersionUID <p>属性的详细说明</p> 
	*/
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String platform;
	private String loginname;
	private Double bet;
	private Double profit;
	private Date starttime;
	private Date endtime;
	private Date updatetime;
	private String tempStarttime;
	private String tempEndtime;
	@Transient
	public String getTempStarttime() {
		return tempStarttime;
	}
	public void setTempStarttime(String tempStarttime) {
		this.tempStarttime = tempStarttime;
	}
	@Transient
	public String getTempEndtime() {
		return tempEndtime;
	}
	public void setTempEndtime(String tempEndtime) {
		this.tempEndtime = tempEndtime;
	}
	

	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
