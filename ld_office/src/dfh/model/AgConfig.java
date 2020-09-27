package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ag_config", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgConfig implements java.io.Serializable {
	private static final long serialVersionUID = -8464120027907992491L;
	// Fields
	
	private String platform;
	private Integer flag;
	private Date createTime;
    private Date lastupdateTime;
    private String remark;

	/** default constructor */
	public AgConfig() {
	}
	
	
	public AgConfig(String platform,Integer flag,Date createTime,Date lastupdateTime) {
		this.platform=platform;
		this.flag=flag;
		this.createTime=createTime;
		this.lastupdateTime=lastupdateTime;

	}
	
     public AgConfig(String platform,Integer flag,Date createTime,Date lastupdateTime,String remark) {
    	 this.flag=flag;
    	 this.platform=platform;
 		 this.flag=flag;
 		 this.createTime=createTime;
 		 this.remark=remark;
	 }

    @Id
	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}


	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "lastupdateTime")
	public Date getLastupdateTime() {
		return lastupdateTime;
	}


	public void setLastupdateTime(Date lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	


	

}