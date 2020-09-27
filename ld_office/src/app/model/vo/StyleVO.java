package app.model.vo;

public class StyleVO {
	
	// 编号
	private Integer id;
	// 活动标题
	private String activityTitle;
	// 活动简介
	private String activitySummary;
	// 活动图片路径
	private String activityImageUrl;
	// 年度（取优惠活动开始时间）
	private String year;
	private String year_month;
	//新增字段
	private String openUrl;
	private String openType;
	
	public String getActivityImageUrl() {
		return activityImageUrl;
	}
	public void setActivityImageUrl(String activityImageUrl) {
		this.activityImageUrl = activityImageUrl;
	}
	public String getYear_month() {
		return year_month;
	}
	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityTitle() {
		return activityTitle;
	}
	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}
	public String getActivitySummary() {
		return activitySummary;
	}
	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getOpenUrl() {
		return openUrl;
	}
	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	
}