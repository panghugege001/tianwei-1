package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "privilege", catalog = "tianwei")
public class Privilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String loginName;                //用户名
	private Date createTime;                 //创建时间
	private Double amount;                   //金额
	private int status;                      //0: 待派发  1：已派发  2：已取消
	private String distributeMonth;          //派发月份
	private Double minDeposit;               //存款要求
	private Double minBet;                   //投注额要求
	private Date startTime;                  //开始时间
	private Date endTime;                    //结束时间
	private Double depositAmount;            //实际存款额
	private Double betAmount;                //实际投注额
	private String remark;                   //备注（活动名称）
	
	public Privilege(){}
	
	public Privilege(String loginName, Date createTime, Double amount, int status, String distributeMonth, 
			Double minDeposit, Double minBet, Date startTime, Date endTime, Double depositAmount, Double betAmount, String remark){
		this.loginName = loginName;
		this.createTime = createTime;
		this.amount = amount;
		this.status = status;
		this.distributeMonth = distributeMonth;
		this.minDeposit = minDeposit;
		this.minBet = minBet;
		this.startTime = startTime;
		this.endTime = endTime;
		this.depositAmount = depositAmount;
		this.betAmount = betAmount;
		this.remark = remark;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "loginname")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@javax.persistence.Column(name = "createtime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@javax.persistence.Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@javax.persistence.Column(name = "distribute_month")
	public String getDistributeMonth() {
		return distributeMonth;
	}
	public void setDistributeMonth(String distributeMonth) {
		this.distributeMonth = distributeMonth;
	}
	
	@javax.persistence.Column(name = "mindeposit")
	public Double getMinDeposit() {
		return minDeposit;
	}
	public void setMinDeposit(Double minDeposit) {
		this.minDeposit = minDeposit;
	}
	
	@javax.persistence.Column(name = "minbet")
	public Double getMinBet() {
		return minBet;
	}
	public void setMinBet(Double minBet) {
		this.minBet = minBet;
	}
	
	@javax.persistence.Column(name = "starttime")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@javax.persistence.Column(name = "endtime")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@javax.persistence.Column(name = "depositAmount")
	public Double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	
	@javax.persistence.Column(name = "betAmount")
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	
	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
