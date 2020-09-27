package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name="slots_match_weekly",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SlotsMatchWeekly {
	
	private Long id;
	private String loginname;
	private Date startTime;
	private Date endTime;
	private Double bet;
	private Double win;
	private String platform;
	private Date getTime;
	
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
	
	@javax.persistence.Column(name = "bet")
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	
	@javax.persistence.Column(name = "win")
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}
	
	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@javax.persistence.Column(name = "getTime")
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
}
