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
@Table(name = "pt_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtData implements java.io.Serializable {

	// Fields
	
	private String id;
	private String playername;
	private String code;
	private String currencycode;
	private String activeplayers;
	private String balancechange;
	private String deposits;
	private String withdraws;
	private String bonuses;
	private String comps;
	private String progressivebets;
	private String progressivewins;
	private Double bets;
	private String wins;
	private Double netloss;
	private String netpurchase;
	private String netgaming;
	private Double houseearnings;
	private String rnum;
	private Date starttime;
	private Date endtime;
	private Date creattime;	


	// Constructors

	/** default constructor */
	public PtData() {
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

	@javax.persistence.Column(name = "PLAYERNAME")
	public String getPlayername() {
		return playername;
	}

	
	public void setPlayername(String playername) {
		this.playername = playername;
	}

	@javax.persistence.Column(name = "CODE")
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@javax.persistence.Column(name = "CURRENCYCODE")
	public String getCurrencycode() {
		return currencycode;
	}


	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	@javax.persistence.Column(name = "ACTIVEPLAYERS")
	public String getActiveplayers() {
		return activeplayers;
	}


	public void setActiveplayers(String activeplayers) {
		this.activeplayers = activeplayers;
	}

	@javax.persistence.Column(name = "BALANCECHANGE")
	public String getBalancechange() {
		return balancechange;
	}


	public void setBalancechange(String balancechange) {
		this.balancechange = balancechange;
	}

	@javax.persistence.Column(name = "DEPOSITS")
	public String getDeposits() {
		return deposits;
	}


	public void setDeposits(String deposits) {
		this.deposits = deposits;
	}

	@javax.persistence.Column(name = "WITHDRAWS")
	public String getWithdraws() {
		return withdraws;
	}


	public void setWithdraws(String withdraws) {
		this.withdraws = withdraws;
	}

	@javax.persistence.Column(name = "BONUSES")
	public String getBonuses() {
		return bonuses;
	}


	public void setBonuses(String bonuses) {
		this.bonuses = bonuses;
	}

	@javax.persistence.Column(name = "COMPS")
	public String getComps() {
		return comps;
	}


	public void setComps(String comps) {
		this.comps = comps;
	}

	@javax.persistence.Column(name = "PROGRESSIVEBETS")
	public String getProgressivebets() {
		return progressivebets;
	}


	public void setProgressivebets(String progressivebets) {
		this.progressivebets = progressivebets;
	}

	@javax.persistence.Column(name = "PROGRESSIVEWINS")
	public String getProgressivewins() {
		return progressivewins;
	}


	public void setProgressivewins(String progressivewins) {
		this.progressivewins = progressivewins;
	}

	@javax.persistence.Column(name = "BETS")
	public Double getBets() {
		return bets;
	}


	public void setBets(Double bets) {
		this.bets = bets;
	}

	@javax.persistence.Column(name = "WINS")
	public String getWins() {
		return wins;
	}


	public void setWins(String wins) {
		this.wins = wins;
	}

	@javax.persistence.Column(name = "NETLOSS")
	public Double getNetloss() {
		return netloss;
	}


	public void setNetloss(Double netloss) {
		this.netloss = netloss;
	}

	@javax.persistence.Column(name = "NETPURCHASE")
	public String getNetpurchase() {
		return netpurchase;
	}


	public void setNetpurchase(String netpurchase) {
		this.netpurchase = netpurchase;
	}

	@javax.persistence.Column(name = "NETGAMING")
	public String getNetgaming() {
		return netgaming;
	}


	public void setNetgaming(String netgaming) {
		this.netgaming = netgaming;
	}

	@javax.persistence.Column(name = "RNUM")
	public String getRnum() {
		return rnum;
	}


	public void setRnum(String rnum) {
		this.rnum = rnum;
	}

	@javax.persistence.Column(name = "STARTTIME")
	public Date getStarttime() {
		return starttime;
	}


	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	@javax.persistence.Column(name = "ENDTIME")
	public Date getEndtime() {
		return endtime;
	}


	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@javax.persistence.Column(name = "CREATTIME")
	public Date getCreattime() {
		return creattime;
	}


	public void setCreattime(Date creattime) {
		this.creattime = creattime;
	}

	@javax.persistence.Column(name = "HOUSEEARNINGS")
	public Double getHouseearnings() {
		return houseearnings;
	}


	public void setHouseearnings(Double houseearnings) {
		this.houseearnings = houseearnings;
	}

}