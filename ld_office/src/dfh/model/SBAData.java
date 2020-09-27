package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sba_data_tw", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SBAData implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	private String transId; //下注id

    private String playerName;//玩家账号

    private String transactionTime;//下注的交易时间

    private String matchDatetime;//比赛开球时间

    private String ticketStatus;//票据状态（Half WON/Half LOSE/WON/LOSE/VOID/running/DRAW/Reject/Refund/Waiting）Half WON/Half LOSE/WON/LOSE/DRAW/Refund状态为已结算

    private String stake;//投注额

    private String winLoseAmount;//最终输赢金额

    private String afterAmount;//结算后的余额

    private String winLostDateTime;//最终下注输赢时间

    @Id
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getMatchDatetime() {
		return matchDatetime;
	}

	public void setMatchDatetime(String matchDatetime) {
		this.matchDatetime = matchDatetime;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getStake() {
		return stake;
	}

	public void setStake(String stake) {
		this.stake = stake;
	}

	public String getWinLoseAmount() {
		return winLoseAmount;
	}

	public void setWinLoseAmount(String winLoseAmount) {
		this.winLoseAmount = winLoseAmount;
	}

	public String getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(String afterAmount) {
		this.afterAmount = afterAmount;
	}

	public String getWinLostDateTime() {
		return winLostDateTime;
	}

	public void setWinLostDateTime(String winLostDateTime) {
		this.winLostDateTime = winLostDateTime;
	}
 
}

