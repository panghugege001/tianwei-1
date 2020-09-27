package app.model.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proposal_extend", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ProposalExtend implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 提案号
	private String pno;
	// 存送平台编号
	private String platform;
	// 自助优惠编号
	private Integer preferentialId;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	
	public ProposalExtend() {}
	
	@Id
	@javax.persistence.Column(name = "pno")
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@javax.persistence.Column(name = "preferential_id")
	public Integer getPreferentialId() {
		return preferentialId;
	}

	public void setPreferentialId(Integer preferentialId) {
		this.preferentialId = preferentialId;
	}
	
	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}