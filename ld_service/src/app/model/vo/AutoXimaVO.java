package app.model.vo;

import java.util.Date;
public class AutoXimaVO {
	private Double rate;			// 洗码率
	private Date startTime;
	private Date endTime;
	private Double ximaAmount;		// 反水金额
	private Double validAmount;		// 有效投注额
	private String message;			// 仅当存在错误时有值
	private String ximaType;		// 洗码类型；自助反水，系统反水
	private String ximaStatus;		// 洗码状态；成功，失败，待处理
	private String pno;				// 洗码编号
	private String statisticsTimeRange; // 统计时间段
	private Double totalValidAmount;// 总有效投注额
	private Double totalXimaAmount;	// 总反水金额
	private int totalCount;
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getXimaAmount() {
		return ximaAmount;
	}
	public void setXimaAmount(Double ximaAmount) {
		this.ximaAmount = ximaAmount;
	}
	public Double getValidAmount() {
		return validAmount;
	}
	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getXimaType() {
		return ximaType;
	}
	public void setXimaType(String ximaType) {
		this.ximaType = ximaType;
	}
	public String getXimaStatus() {
		return ximaStatus;
	}
	public void setXimaStatus(String ximaStatus) {
		this.ximaStatus = ximaStatus;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getStatisticsTimeRange() {
		return statisticsTimeRange;
	}
	public void setStatisticsTimeRange(String statisticsTimeRange) {
		this.statisticsTimeRange = statisticsTimeRange;
	}
	public Double getTotalValidAmount() {
		return totalValidAmount;
	}
	public void setTotalValidAmount(Double totalValidAmount) {
		this.totalValidAmount = totalValidAmount;
	}
	public Double getTotalXimaAmount() {
		return totalXimaAmount;
	}
	public void setTotalXimaAmount(Double totalXimaAmount) {
		this.totalXimaAmount = totalXimaAmount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
