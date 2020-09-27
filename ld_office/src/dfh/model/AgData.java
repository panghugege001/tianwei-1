package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AgData entity.
 * 
 */
@Entity
@Table(name = "agdata", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	
	private String billNo;//流水号
	private String gameCode;//局号
	private String playName;//玩家账号
	private Double betAmount;//投注
	private Double validBetAmount;//有效投注额
	private Double netAmount;//派彩
	private Double beforeCredit;//下注前额度
	private Date betTime;//下注时间
	private Date recalcuTime;//派彩时间
	private String gameType;//游戏类型
	private String deviceType;//设备类型
	private String platformType;//平台类型
	
	
	
	@Id
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Date getBetTime() {
		return betTime;
	}
	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	public Double getValidBetAmount() {
		return validBetAmount;
	}
	public void setValidBetAmount(Double validBetAmount) {
		this.validBetAmount = validBetAmount;
	}
	public Date getRecalcuTime() {
		return recalcuTime;
	}
	public void setRecalcuTime(Date recalcuTime) {
		this.recalcuTime = recalcuTime;
	}
	public Double getBeforeCredit() {
		return beforeCredit;
	}
	public void setBeforeCredit(Double beforeCredit) {
		this.beforeCredit = beforeCredit;
	}
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		if (getClass() != o.getClass()) {
			return false;
		}
		AgData agData = (AgData) o;
		if (billNo == null) {
			if (agData.billNo != null) {
				return false;
			}
		} else {
			if (!billNo.equals(agData.billNo)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 1;
	}

}