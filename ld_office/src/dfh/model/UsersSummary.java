package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import dfh.utils.AESUtil;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "usersSummary", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UsersSummary implements java.io.Serializable {

	// Fields
	
	private Integer id;
	private String loginname;
	private Date lastLoginTime;
	private String role;
	private Integer flag;
	private Integer loginerrornum;
	private Double money;
	private Double credit;
	private Double slotaccount;
	private Double pt;
	private Double slot;
	private Double ag;
	private Double ttg;
	private Double n2live;
	private Double ebet;
	private Double mwg;
	private Double bbin;
	private Double chess;
	private Double sbaSport;
	private Double ysbSport;
	private Double imSport;
	private Double pbSport;
	private Double fish;
	private Double fanya;
	private Double minibit;
	private Double vr;
	private Double kyqp;
	
	private Date createtime;

	
	
	public UsersSummary(){
		
	}


  
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
   public String getLoginname() {
		return loginname;
	}



	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}



	public Date getLastLoginTime() {
		return lastLoginTime;
	}



	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}



	public Double getCredit() {
		return credit;
	}



	public void setCredit(Double credit) {
		this.credit = credit;
	}



	public Double getSlotaccount() {
		return slotaccount;
	}



	public void setSlotaccount(Double slotaccount) {
		this.slotaccount = slotaccount;
	}



	public Double getPt() {
		return pt;
	}



	public void setPt(Double pt) {
		this.pt = pt;
	}



	public Double getSlot() {
		return slot;
	}



	public void setSlot(Double slot) {
		this.slot = slot;
	}



	public Double getSbaSport() {
		return sbaSport;
	}



	public void setSbaSport(Double sbaSport) {
		this.sbaSport = sbaSport;
	}



	public Double getYsbSport() {
		return ysbSport;
	}



	public void setYsbSport(Double ysbSport) {
		this.ysbSport = ysbSport;
	}



	public Double getImSport() {
		return imSport;
	}



	public void setImSport(Double imSport) {
		this.imSport = imSport;
	}



	public Double getPbSport() {
		return pbSport;
	}



	public void setPbSport(Double pbSport) {
		this.pbSport = pbSport;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public Date getCreatetime() {
		return createtime;
	}



	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}



	public Integer getLoginerrornum() {
		return loginerrornum;
	}



	public void setLoginerrornum(Integer loginerrornum) {
		this.loginerrornum = loginerrornum;
	}



	public Integer getFlag() {
		return flag;
	}



	public void setFlag(Integer flag) {
		this.flag = flag;
	}



	public Double getAg() {
		return ag;
	}



	public void setAg(Double ag) {
		this.ag = ag;
	}



	public Double getTtg() {
		return ttg;
	}



	public void setTtg(Double ttg) {
		this.ttg = ttg;
	}



	public Double getN2live() {
		return n2live;
	}



	public void setN2live(Double n2live) {
		this.n2live = n2live;
	}



	public Double getEbet() {
		return ebet;
	}



	public void setEbet(Double ebet) {
		this.ebet = ebet;
	}



	public Double getMwg() {
		return mwg;
	}



	public void setMwg(Double mwg) {
		this.mwg = mwg;
	}



	public Double getBbin() {
		return bbin;
	}



	public void setBbin(Double bbin) {
		this.bbin = bbin;
	}



	public Double getChess() {
		return chess;
	}



	public void setChess(Double chess) {
		this.chess = chess;
	}



	public Double getFish() {
		return fish;
	}



	public void setFish(Double fish) {
		this.fish = fish;
	}



	public Double getFanya() {
		return fanya;
	}



	public void setFanya(Double fanya) {
		this.fanya = fanya;
	}


	public Double getMinibit() {
		return minibit;
	}


	public void setMinibit(Double minibit) {
		this.minibit = minibit;
	}

	public Double getVr() {
		return vr;
	}

	public void setVr(Double vr) {
		this.vr = vr;
	}

	public Double getKyqp() {
		return kyqp;
	}

	public void setKyqp(Double kyqp) {
		this.kyqp = kyqp;
	}



	public Double getMoney() {
		return money;
	}



	public void setMoney(Double money) {
		this.money = money;
	}
	
	
}