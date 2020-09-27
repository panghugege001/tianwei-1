package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="preferential_record",catalog="tianwei"
)
public class PreferentialRecord {
	private String pno ;
	private String loginname ; ;
	private String platform ;
	private Double validBet ;
	private Date createtime ;
	private Integer type ;
	
	public PreferentialRecord(String pno, String loginname, String platform,
			Double validBet, Date createtime) {
		super();
		this.pno = pno;
		this.loginname = loginname;
		this.platform = platform;
		this.validBet = validBet;
		this.createtime = createtime;
	}
	
	
	public PreferentialRecord() {
	}


	@Id
	@javax.persistence.Column(name = "pno")
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@javax.persistence.Column(name = "validBet")
	public Double getValidBet() {
		return validBet;
	}
	public void setValidBet(Double validBet) {
		this.validBet = validBet;
	}
	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}
	
	

}
