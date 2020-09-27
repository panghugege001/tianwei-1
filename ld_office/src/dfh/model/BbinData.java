package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Jalen 2018-05-18
 */
@Entity
@Table(name = "bbin_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BbinData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;//	帐号
	private Integer wagersID;//	注单号码
	private Date wagersDate;//	下注时间
	private String gameType;//	游戏种类 electronic 电子，video真人
	//	private String result;//	注单结果 -1：注销、1：赢、200：输、0：未结算、-77：未结算
	private Double betAmount;//	下注金额
	private Double payoff;//	派彩金额
	private Double commissionable;//	会员有效投注额


	// Constructors

	/** default constructor */
	public BbinData() {
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Id
	public Integer getWagersID() {
		return wagersID;
	}


	public void setWagersID(Integer wagersID) {
		this.wagersID = wagersID;
	}


	public Date getWagersDate() {
		return wagersDate;
	}


	public void setWagersDate(Date wagersDate) {
		this.wagersDate = wagersDate;
	}


	public String getGameType() {
		return gameType;
	}


	public void setGameType(String gameType) {
		this.gameType = gameType;
	}


	public Double getBetAmount() {
		return betAmount;
	}


	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}


	public Double getPayoff() {
		return payoff;
	}


	public void setPayoff(Double payoff) {
		this.payoff = payoff;
	}


	public Double getCommissionable() {
		return commissionable;
	}


	public void setCommissionable(Double commissionable) {
		this.commissionable = commissionable;
	}
}