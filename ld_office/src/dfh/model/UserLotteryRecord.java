package dfh.model;

import java.util.Date;  

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_lottery_record", catalog = "tianwei")

@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UserLotteryRecord implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private String loginname;
	private String itemName;
	private int isReceive;
	private Date winningDate;
	private Date receiveDate;
	private int period;
	// Constructors

	/** default constructor */
	public UserLotteryRecord() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@javax.persistence.Column(name = "id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "item_name")
	public String getItemName() {
		return itemName;
	}

	@javax.persistence.Column(name = "is_receive")
	public int getIsReceive() {
		return isReceive;
	}

	@javax.persistence.Column(name = "winning_date")
	public Date getWinningDate() {
		return winningDate;
	}

	@javax.persistence.Column(name = "receive_date")
	public Date getReceiveDate() {
		return receiveDate;
	}

	@javax.persistence.Column(name = "period")
	public int getPeriod() {
		return period;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public void setIsReceive(int isReceive) {
		this.isReceive = isReceive;
	}


	public void setWinningDate(Date winningDate) {
		this.winningDate = winningDate;
	}


	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}


	public void setPeriod(int period) {
		this.period = period;
	}

}