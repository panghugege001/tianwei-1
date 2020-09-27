package com.gsmc.png.model.ag;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gsmc.png.model.shaba.SBAData4OracleVO;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "AG_DATA_LD")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgDataLD extends AGData4OracleVO   implements java.io.Serializable {
	private static final long serialVersionUID = -8772256477280610100L;

	private String billNo;    //billno
	private String playerName;
	private Double beforeCredit;    
	private Double betamount;   //投注额度
	private Double validbetamount;   //有投注额度
	private Double netamount;   //玩家输赢
	private Double jackpot; //投注重新派彩时间
	private Date betTime;    //billno
	private Date lastupdate;    //
	private Date createtime;    //beforeCredit
	private String gameType;    //billno
	private String platform; 



	// Constructors

	/** default constructor */
	public AgDataLD() {
	}
	

	@Id
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}


	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	
	public Double getBeforeCredit() {
		return beforeCredit;
	}

	public void setBeforeCredit(Double beforeCredit) {
		this.beforeCredit = beforeCredit;
	}

	public Date getLastupdate() {
		return lastupdate;
	}
	
	
	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}


	public Double getBetamount() {
		return betamount;
	}


	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}

	public Double getValidbetamount() {
		return validbetamount;
	}


	public void setValidbetamount(Double validbetamount) {
		this.validbetamount = validbetamount;
	}

	public Double getNetamount() {
		return netamount;
	}


	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}

	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Double getJackpot() {
		return jackpot;
	}


	public void setJackpot(Double jackpot) {
		this.jackpot = jackpot;
	}


	public Date getBetTime() {
		return betTime;
	}


	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}

	
     
}