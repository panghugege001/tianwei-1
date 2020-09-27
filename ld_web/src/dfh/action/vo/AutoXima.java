package dfh.action.vo;

import java.io.Serializable;
import java.util.Date;
import dfh.utils.DateUtil;

public class AutoXima implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5445196776759840089L;
	private Double rate;			// 洗码率
	private Date startTime;
	private Date endTime;
	private String startTimeStr;
	private String endTimeStr;
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
	private String jsonResult;
	private String platfrom;
	
	public AutoXima() {
		// TODO Auto-generated constructor stub
	}
	
	public AutoXima(String message) {
		super();
		this.message = message;
	}
	
	public AutoXima(String jsonResult,String message) {
		super();
	     this.jsonResult=jsonResult;
	     this.message=message;
	}
	
	
	public AutoXima(Double totalValidAmount, Double totalXimaAmount,int totalCount) {
		super();
		this.totalValidAmount = totalValidAmount;
		this.totalXimaAmount = totalXimaAmount;
		this.totalCount = totalCount;
	}

	
	
	public AutoXima(Double rate, String startTimeStr, String endTimeStr,Double ximaAmount, Double validAmount) {
		super();
		this.rate = rate;
		this.startTimeStr = startTimeStr;
		this.endTimeStr = endTimeStr;
		this.ximaAmount = ximaAmount;
		this.validAmount = validAmount;
	}
	

	
	public AutoXima(Double rate, Double ximaAmount, Double validAmount,String message) {
		super();
		this.rate = rate;
		this.ximaAmount = ximaAmount;
		this.validAmount = validAmount;
		this.message = message;
	}
	
	

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setStatisticsTimeRange(Date startTime,Date endTime) {
		this.statisticsTimeRange =DateUtil.formatDateForStandard(startTime)+"至"+DateUtil.formatDateForStandard(endTime);
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
	
	public String getStatisticsTimeRange() {
		return statisticsTimeRange;
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
	
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
	

	public String getPlatfrom() {
		return platfrom;
	}

	public void setPlatfrom(String platfrom) {
		this.platfrom = platfrom;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	

}
