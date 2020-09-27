package dfh.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;

@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DataGatherDetailId implements Serializable {

	private static final long serialVersionUID = 1L;

	// 会员账号
	private String loginName;
	// 创建时间
	private Date createTime;

	public DataGatherDetailId() {

	}

	public DataGatherDetailId(String loginName, Date createTime) {

		this.loginName = loginName;
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}