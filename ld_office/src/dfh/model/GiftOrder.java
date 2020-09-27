package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gift_order", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class GiftOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer giftID;          //申请的礼品
	private String loginname;        //账号
	private Integer level;           //等级
	private String realname;         //姓名
	private String cellphoneNo;      //手机号
	private String address;          //地址
	private int status;              //状态
	private Date applyDate;          //申请时间
	
	public GiftOrder(){}
	
	public GiftOrder(Integer giftID, String loginname, Integer level, String realname, String cellphoneNo, String address, int status, Date applyDate){
		this.giftID = giftID;
		this.loginname = loginname;
		this.level = level;
		this.realname = realname;
		this.cellphoneNo = cellphoneNo;
		this.address = address;
		this.status = status;
		this.applyDate = applyDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "giftID")
	public Integer getGiftID() {
		return giftID;
	}

	public void setGiftID(Integer giftID) {
		this.giftID = giftID;
	}

	@Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "realname")
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "cellphoneNo")
	public String getCellphoneNo() {
		return cellphoneNo;
	}

	public void setCellphoneNo(String cellphoneNo) {
		this.cellphoneNo = cellphoneNo;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "applyDate")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
}
