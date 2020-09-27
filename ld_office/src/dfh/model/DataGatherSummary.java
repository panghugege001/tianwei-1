package dfh.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "data_gather_summary", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DataGatherSummary {

	// 会员账号
	private String loginName;
	// 所属代理账号
	private String agent;
	// 存款
	private Double deposit;
	// 在线支付
	private Double pay;
	// 提款
	private Double withdrawal;
	// 好友推荐奖金
	private Double friendBonus;
	// 总存款
	private Double totalDeposit;
	// 盈利额
	private Double profit;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;

	public DataGatherSummary() {

	}

	@Id
	@javax.persistence.Column(name = "login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@javax.persistence.Column(name = "deposit")
	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	@javax.persistence.Column(name = "pay")
	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	@javax.persistence.Column(name = "withdrawal")
	public Double getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(Double withdrawal) {
		this.withdrawal = withdrawal;
	}

	@javax.persistence.Column(name = "friend_bonus")
	public Double getFriendBonus() {
		return friendBonus;
	}

	public void setFriendBonus(Double friendBonus) {
		this.friendBonus = friendBonus;
	}

	@javax.persistence.Column(name = "total_deposit")
	public Double getTotalDeposit() {
		return totalDeposit;
	}

	public void setTotalDeposit(Double totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	@javax.persistence.Column(name = "profit")
	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
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