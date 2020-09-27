package dfh.model;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


@Entity
@Table(name = "ptstatistical", catalog = "longdu_email")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
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
	private String ntName;

	public PtStatistical() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@javax.persistence.Column(name = "line")
	public Double getLine() {
		return line;
	}

	public void setLine(Double line) {
		this.line = line;
	}

	@javax.persistence.Column(name = "bet")
	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	@javax.persistence.Column(name = "multiplier")
	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	@javax.persistence.Column(name = "gameCode")
	public Integer getGameCode() {
		return gameCode;
	}

	public void setGameCode(Integer gameCode) {
		this.gameCode = gameCode;
	}

	@javax.persistence.Column(name = "payOut")
	public Double getPayOut() {
		return payOut;
	}

	public void setPayOut(Double payOut) {
		this.payOut = payOut;
	}

	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "userId")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "playtime")
	public Date getPlaytime() {
		return playtime;
	}

	public void setPlaytime(Date playtime) {
		this.playtime = playtime;
	}

	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public String toString() {
		return "游戏投注数据 [id=" + id + ", userId=" + userId + ", playtime="
				+ playtime + ", amount=" + amount + ", line=" + line + ", bet="
				+ bet + ", multiplier=" + multiplier + ", gameCode=" + gameCode
				+ ", payOut=" + payOut + ", type=" + type + ", createtime="
				+ createtime + ", loginname=" + loginname + ", ntName="+ ntName +"]";
	}
	
	@Transient
	public String getNtName() {
		return ntName;
	}
	
	public void setNtName(String ntName) {
		this.ntName = ntName;
	}
	
	/*public static void main(String[] args) {
		Workbook readwb = null;
		try {
			InputStream instream = new FileInputStream("C:/Users/parry/Desktop/NT老虎机/NT游戏列表xls2.xls");
			readwb = Workbook.getWorkbook(instream);
			Sheet readsheet = readwb.getSheet(0);
			int rsColumns = readsheet.getColumns();
			int rsRows = readsheet.getRows();
			for (int i = 0; i < rsRows; i++) {
				Cell english = readsheet.getCell(0, i);
				Cell chinese = readsheet.getCell(3, i);
				Cell code = readsheet.getCell(4, i);
				System.out.println(english.getContents().toUpperCase().replaceAll(" ", "")+"("+code.getContents()+",\""+chinese.getContents()+"\"),");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}