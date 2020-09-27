package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userupgradelog" ,catalog="tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class UpgradeLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;  
	private String username;
	private Double bet;         //投注额
	private Double ptBet;       //pt投注额
	private Double betOfWeek;   //周投注额
	private Double ptBetOfWeek; //周PT投注额
	private Date createTime;    //创建时间
	private Integer oldlevel;       //原等级
	private Integer newlevel;       //新等级
	private String status;     //状态：0 已取消  1：已处理 2：待确认
	private String optmonth;    //处理月份
	private String remark;      //备注
	
	public UpgradeLog(){}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="bet")
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	
	@Column(name="ptBet")
	public Double getPtBet() {
		return ptBet;
	}
	public void setPtBet(Double ptBet) {
		this.ptBet = ptBet;
	}
	
	@Column(name="bet_week")
	public Double getBetOfWeek() {
		return betOfWeek;
	}
	public void setBetOfWeek(Double betOfWeek) {
		this.betOfWeek = betOfWeek;
	}

	@Column(name="ptbet_week")
	public Double getPtBetOfWeek() {
		return ptBetOfWeek;
	}
	public void setPtBetOfWeek(Double ptBetOfWeek) {
		this.ptBetOfWeek = ptBetOfWeek;
	}

	@Column(name="createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="oldlevel")
	public Integer getOldlevel() {
		return oldlevel;
	}
	public void setOldlevel(Integer oldlevel) {
		this.oldlevel = oldlevel;
	}
	
	@Column(name="newlevel")
	public Integer getNewlevel() {
		return newlevel;
	}
	public void setNewlevel(Integer newlevel) {
		this.newlevel = newlevel;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="optmonth")
	public String getOptmonth() {
		return optmonth;
	}
	public void setOptmonth(String optmonth) {
		this.optmonth = optmonth;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
