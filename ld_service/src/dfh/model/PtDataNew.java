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
@Table(name = "pt_data_new", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtDataNew implements java.io.Serializable {

	// Fields
	
	private String id;
	private String playername;
	private String fullname;
	private String viplevel;
	private String country;
	private String games;
	private String currencycode;
	private Double bets;
	private Double wins;
	private Double income;
	private Integer rnum;
	private Date starttime;
	private Date endtime;
	private Date creattime;	

	private Double betsTiger;
	private Double winsTiger;
	
	private Double progressiveBet;
	private Double progressiveWin;

	/** default constructor */
	public PtDataNew() {
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

	@javax.persistence.Column(name = "FULLNAME")
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@javax.persistence.Column(name = "VIPLEVEL")
	public String getViplevel() {
		return viplevel;
	}

	public void setViplevel(String viplevel) {
		this.viplevel = viplevel;
	}

	@javax.persistence.Column(name = "COUNTRY")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@javax.persistence.Column(name = "GAMES")
	public String getGames() {
		return games;
	}

	public void setGames(String games) {
		this.games = games;
	}

	@javax.persistence.Column(name = "CURRENCYCODE")
	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	@javax.persistence.Column(name = "BETS")
	public Double getBets() {
		return bets;
	}

	public void setBets(Double bets) {
		this.bets = bets;
	}

	@javax.persistence.Column(name = "WINS")
	public Double getWins() {
		return wins;
	}

	public void setWins(Double wins) {
		this.wins = wins;
	}

	@javax.persistence.Column(name = "INCOME")
	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	@javax.persistence.Column(name = "RNUM")
	public Integer getRnum() {
		return rnum;
	}

	public void setRnum(Integer rnum) {
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

	@javax.persistence.Column(name = "CREATETIME")
	public Date getCreattime() {
		return creattime;
	}

	public void setCreattime(Date creattime) {
		this.creattime = creattime;
	}

	@javax.persistence.Column(name = "BETS_TIGER")
	public Double getBetsTiger() {
		return betsTiger;
	}

	public void setBetsTiger(Double betsTiger) {
		this.betsTiger = betsTiger;
	}

	@javax.persistence.Column(name = "WINS_TIGER")
	public Double getWinsTiger() {
		return winsTiger;
	}

	public void setWinsTiger(Double winsTiger) {
		this.winsTiger = winsTiger;
	}
	
	@javax.persistence.Column(name = "PROGRESSIVE_BETS")
	public Double getProgressiveBet() {
		return progressiveBet;
	}

	public void setProgressiveBet(Double progressiveBet) {
		this.progressiveBet = progressiveBet;
	}

	@javax.persistence.Column(name = "PROGRESSIVE_WINS")
	public Double getProgressiveWin() {
		return progressiveWin;
	}

	public void setProgressiveWin(Double progressiveWin) {
		this.progressiveWin = progressiveWin;
	}
}
