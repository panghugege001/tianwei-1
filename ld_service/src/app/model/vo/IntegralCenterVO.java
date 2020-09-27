package app.model.vo;

public class IntegralCenterVO  {
	private String integral;			// 可用积分
	private String historyIntegral;			// 历史总积分
	private String ratio;		// 兑换比例
	
	public String getHistoryIntegral() {
		return historyIntegral;
	}
	public void setHistoryIntegral(String historyIntegral) {
		this.historyIntegral = historyIntegral;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}
