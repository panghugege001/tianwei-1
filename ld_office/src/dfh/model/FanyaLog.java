package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "fanya_log", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FanyaLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer	id;
	//订单编号
	private String orderId;
	//用户名
	private String userName;
	//游戏类型
	private String category;
	//联赛类型
	private String league;
	//赛事名称
	private String match1;	
	//所参与的投注
	private String bet;
	//投注内容
	private String content;	
	//投注金额
	private Double betAmount;
	//状态
	private String status;	
	//开奖结果
	private String result;
	//盈亏
	private Double money;
	//状态最新更改的时间
	private Date updateAt;
	//投注时间
	private Date createAt;
	//比赛开始时间
	private Date startAt;
	//比赛结束时间
	private Date endAt;
	//开奖时间
	private Date resultAt;
	//派彩時間（結算時間）
	private Date rewardAt;
	
	@Column(name="result_at")
	public Date getResultAt() {
		return resultAt;
	}

	public void setResultAt(Date resultAt) {
		this.resultAt = resultAt;
	}
	@Column(name="reward_at")
	public Date getRewardAt() {
		return rewardAt;
	}
	
	public void setRewardAt(Date rewardAt) {
		this.rewardAt = rewardAt;
	}

	//创建时间
	private Date createTime;

	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="order_id")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Column(name="user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	@Column(name="league")
	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}
	@Column(name="match1")
	public String getMatch1() {
		return match1;
	}

	public void setMatch1(String match1) {
		this.match1 = match1;
	}
	@Column(name="bet")
	public String getBet() {
		return bet;
	}

	public void setBet(String bet) {
		this.bet = bet;
	}
	@Column(name="content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="bet_amount")
	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	@Column(name="money")
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	@Column(name="update_at")
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	@Column(name="create_at")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="start_at")
	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	@Column(name="end_at")
	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

}