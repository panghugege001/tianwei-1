package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "betrank", catalog = "tianwei")
public class BetRankModel implements java.io.Serializable {

	private String loginname;
	
	private String amount;
	
	private String address;
	
	private String platform;
	
	private String remark;
	
	private int id;
	
	
	private int no;
	
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@javax.persistence.Column(name = "sumbet")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	@javax.persistence.Column(name = "lastarea")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@javax.persistence.Column(name = "type")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
	@Id
	@javax.persistence.Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}