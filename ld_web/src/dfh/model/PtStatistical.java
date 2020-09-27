package dfh.model;
import java.sql.Timestamp;
import java.util.Date;


public class PtStatistical implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2798243057782110568L;
	private Integer id;
	private Integer userId;
	private Date playtime;
	private Double amount;
	private Double line;
	private Double bet;
	private Double multiplier;
	private Integer gameCode;
	private Double payOut;
	private Integer type;
	private Date createtime;
	private String loginname;

	public PtStatistical() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getPlaytime() {
		return playtime;
	}

	public void setPlaytime(Date playtime) {
		this.playtime = playtime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getLine() {
		return line;
	}

	public void setLine(Double line) {
		this.line = line;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	public Integer getGameCode() {
		return gameCode;
	}

	public void setGameCode(Integer gameCode) {
		this.gameCode = gameCode;
	}

	public Double getPayOut() {
		return payOut;
	}

	public void setPayOut(Double payOut) {
		this.payOut = payOut;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	
	

}