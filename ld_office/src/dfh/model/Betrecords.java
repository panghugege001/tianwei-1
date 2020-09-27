package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Betrecords entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "betrecords", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Betrecords implements java.io.Serializable {

	// Fields

	private String billNo;
	private Timestamp billTime;
	private String gmCode;
	private String passport;
	private String drawNo;
	private String playCode;
	private Double billAmount;
	private Double result;

	// Constructors

	/** default constructor */
	public Betrecords() {
	}

	/** minimal constructor */
	public Betrecords(String billNo, Timestamp billTime) {
		this.billNo = billNo;
		this.billTime = billTime;
	}

	/** full constructor */
	public Betrecords(String billNo, Timestamp billTime, String gmCode, String passport, String drawNo, String playCode, Double billAmount, Double result) {
		this.billNo = billNo;
		this.billTime = billTime;
		this.gmCode = gmCode;
		this.passport = passport;
		this.drawNo = drawNo;
		this.playCode = playCode;
		this.billAmount = billAmount;
		this.result = result;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "billNo")
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@javax.persistence.Column(name = "billTime")
	public Timestamp getBillTime() {
		return this.billTime;
	}

	public void setBillTime(Timestamp billTime) {
		this.billTime = billTime;
	}

	@javax.persistence.Column(name = "gmCode")
	public String getGmCode() {
		return this.gmCode;
	}

	public void setGmCode(String gmCode) {
		this.gmCode = gmCode;
	}

	@javax.persistence.Column(name = "passport")
	public String getPassport() {
		return this.passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	@javax.persistence.Column(name = "drawNo")
	public String getDrawNo() {
		return this.drawNo;
	}

	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	@javax.persistence.Column(name = "playCode")
	public String getPlayCode() {
		return this.playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	@javax.persistence.Column(name = "billAmount")
	public Double getBillAmount() {
		return this.billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	@javax.persistence.Column(name = "result")
	public Double getResult() {
		return this.result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

}