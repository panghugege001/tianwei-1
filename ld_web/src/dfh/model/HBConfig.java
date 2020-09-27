package dfh.model;

import java.util.Date;

public class HBConfig implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title; // 优惠名称
	private Double limitStartMoney; // 存款下限金额
	private Double limitEndMoney; // 存款上限金额
	private Integer betMultiples; // 流水倍数要求
	private Integer isused; // 是否开启
	private Double amount; // 红包金额
	private Date createtime; // 创建时间
	private Date updatetime; // 更新时间
	private Date starttime; // 启用开始时间
	private Date endtime; // 启用结束时间
	private Integer times; // 可使用次数
	private Integer timesflag; // 使用频率 （1.天 2.周 3.月 4.年'）
	private String vip; // vip等级

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(Integer betMultiples) {
		this.betMultiples = betMultiples;
	}

	public Double getLimitStartMoney() {
		return limitStartMoney;
	}

	public void setLimitStartMoney(Double limitStartMoney) {
		this.limitStartMoney = limitStartMoney;
	}

	public Double getLimitEndMoney() {
		return limitEndMoney;
	}

	public void setLimitEndMoney(Double limitEndMoney) {
		this.limitEndMoney = limitEndMoney;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getIsused() {
		return isused;
	}

	public void setIsused(Integer isused) {
		this.isused = isused;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getTimesflag() {
		return timesflag;
	}

	public void setTimesflag(Integer timesflag) {
		this.timesflag = timesflag;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

}
