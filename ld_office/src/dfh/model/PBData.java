package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "pb_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PBData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer	id;
	
	private String playerId;//玩家账号

	private String betId;//注单编号

	private String stakeAmount;//投注金额

	private String winloss;//输赢值

	private String isSettle;//是否结算
	
	private Date createTime;//注单创建时间
	
	private Date settleTime;//结算时间

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "player_id")
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Column(name = "bet_id")
	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	@Column(name = "stake_amount")
	public String getStakeAmount() {
		return stakeAmount;
	}

	public void setStakeAmount(String stakeAmount) {
		this.stakeAmount = stakeAmount;
	}

	@Column(name = "winloss")
	public String getWinloss() {
		return winloss;
	}

	public void setWinloss(String winloss) {
		this.winloss = winloss;
	}

	@Column(name = "is_settle")
	public String getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(String isSettle) {
		this.isSettle = isSettle;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "settle_time")
	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}
	
}