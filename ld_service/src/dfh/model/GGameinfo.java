package dfh.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GGameinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "g_gameinfo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class GGameinfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2995292015401999544L;
	private Integer id;
	private Timestamp starttime;
	private Timestamp endtime;
	private Timestamp addtime;
	private String code;
	private String gamecode;
	private String loginname;
	private Double betamount;
	private Double hold;
	private Double validbetamount;
	private String result;
	private Integer flag;
	private String betdetail;

	// Constructors

	/** default constructor */
	public GGameinfo() {
	}
	

	public GGameinfo(Timestamp starttime, Timestamp endtime, Timestamp addtime, String code,
			String gamecode, String loginname, Double betamount, Double hold,
			Double validbetamount, String result,String betdetail) {
		this.starttime = starttime;
		this.endtime = endtime;
		this.addtime=addtime;
		this.code = code;
		this.gamecode = gamecode;
		this.loginname = loginname;
		this.betamount = betamount;
		this.hold = hold;
		this.validbetamount = validbetamount;
		this.result = result;
		this.betdetail=betdetail;
	}


	/** minimal constructor */
	public GGameinfo(Timestamp starttime, Timestamp endtime, Timestamp addtime,
			String code, String gamecode, String loginname, Integer flag) {
		this.starttime = starttime;
		this.endtime = endtime;
		this.addtime = addtime;
		this.code = code;
		this.gamecode = gamecode;
		this.loginname = loginname;
		this.flag = flag;
	}

	/** full constructor */
	public GGameinfo(Timestamp starttime, Timestamp endtime, Timestamp addtime,
			String code, String gamecode, String loginname, Double betamount,
			Double hold, Double validbetamount, String result, Integer flag,
			String betdetail) {
		this.starttime = starttime;
		this.endtime = endtime;
		this.addtime = addtime;
		this.code = code;
		this.gamecode = gamecode;
		this.loginname = loginname;
		this.betamount = betamount;
		this.hold = hold;
		this.validbetamount = validbetamount;
		this.result = result;
		this.flag = flag;
		this.betdetail = betdetail;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "starttime")
	public Timestamp getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	@javax.persistence.Column(name = "endtime")
	public Timestamp getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	@javax.persistence.Column(name = "addtime")
	public Timestamp getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	@javax.persistence.Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@javax.persistence.Column(name = "gamecode")
	public String getGamecode() {
		return this.gamecode;
	}

	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "betamount")
	public Double getBetamount() {
		return this.betamount;
	}

	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}

	@javax.persistence.Column(name = "hold")
	public Double getHold() {
		return this.hold;
	}

	public void setHold(Double hold) {
		this.hold = hold;
	}

	@javax.persistence.Column(name = "validbetamount")
	public Double getValidbetamount() {
		return this.validbetamount;
	}

	public void setValidbetamount(Double validbetamount) {
		this.validbetamount = validbetamount;
	}

	@javax.persistence.Column(name = "result")
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "betdetail")
	public String getBetdetail() {
		return this.betdetail;
	}

	public void setBetdetail(String betdetail) {
		this.betdetail = betdetail;
	}

}