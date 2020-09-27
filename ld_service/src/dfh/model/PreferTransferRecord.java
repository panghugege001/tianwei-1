package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="prefer_transfer_record",catalog="tianwei"
)
public class PreferTransferRecord {
	private String id ;
	private String loginname ; ;
	private String platform ;
	private Double nowbet ;
	private Double needbet ;
	private Date createtime ;
	private String remark ;
	private String type ;
	
	
	public PreferTransferRecord() {
	}
	
	public PreferTransferRecord(String loginname, String platform,
			Double nowbet, Double needbet, Date createtime, String remark,String type ) {
		super();
		this.loginname = loginname;
		this.platform = platform;
		this.nowbet = nowbet;
		this.needbet = needbet;
		this.createtime = createtime;
		this.remark = remark;
		this.type = type;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	@javax.persistence.Column(name = "nowbet")
	public Double getNowbet() {
		return nowbet;
	}
	public void setNowbet(Double nowbet) {
		this.nowbet = nowbet;
	}
	@javax.persistence.Column(name = "needbet")
	public Double getNeedbet() {
		return needbet;
	}
	public void setNeedbet(Double needbet) {
		this.needbet = needbet;
	}
	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
