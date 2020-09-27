package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "agdata", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgDataRecord implements java.io.Serializable {
	private static final long serialVersionUID = -8772256477280610100L;
	
	private String id;    //billno
	private String playerName;
	private Integer betcount;   //投注笔数
	private Double betamount;   //投注额度
	private Double validbetamount;   //有投注额度
	private Double netamount;   //玩家输赢
	private Double jackpot; //投注重新派彩时间
	private String platform; 
	private Date createTime; //录入数据时间
	private Date startTime; //
	private Date endTime; //


	// Constructors

	/** default constructor */
	public AgDataRecord() {
	}
	
	public AgDataRecord(String playerName,Integer betcount,Double betamount,Double validbetamount,Double netamount,Double jackpot,String platform,Date createTime,Date startTime,Date endTime) {
		this.playerName=playerName;
		this.betcount=betcount;
		this.betamount=betamount;
		this.validbetamount=validbetamount;
		this.netamount=netamount;
		this.jackpot=jackpot;
		this.platform=platform;
		this.createTime=createTime;
		this.startTime=startTime;
		this.endTime=endTime;
		
	}
	


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "playerName")
	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@javax.persistence.Column(name = "betcount")
	public Integer getBetcount() {
		return betcount;
	}


	public void setBetcount(Integer betcount) {
		this.betcount = betcount;
	}

	@javax.persistence.Column(name = "betamount")
	public Double getBetamount() {
		return betamount;
	}


	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}

	@javax.persistence.Column(name = "validbetamount")
	public Double getValidbetamount() {
		return validbetamount;
	}


	public void setValidbetamount(Double validbetamount) {
		this.validbetamount = validbetamount;
	}

	@javax.persistence.Column(name = "netamount")
	public Double getNetamount() {
		return netamount;
	}


	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}

	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@javax.persistence.Column(name = "jackpot")
	public Double getJackpot() {
		return jackpot;
	}


	public void setJackpot(Double jackpot) {
		this.jackpot = jackpot;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Override
	public String toString() {
		return "AgDataRecord [id=" + id + ", playerName=" + playerName
				+ ", betcount=" + betcount + ", betamount=" + betamount
				+ ", validbetamount=" + validbetamount + ", netamount="
				+ netamount + ", jackpot=" + jackpot + ", platform=" + platform
				+ ", createTime=" + createTime + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}

     
}