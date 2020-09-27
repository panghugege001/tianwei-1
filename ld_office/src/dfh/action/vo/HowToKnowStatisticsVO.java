package dfh.action.vo;

import java.io.Serializable;

public class HowToKnowStatisticsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 735514639864471402L;
	private String howToKnow;
	private Integer count;
	public HowToKnowStatisticsVO(String howToKnow, Integer count) {
		super();
		this.howToKnow = howToKnow;
		this.count = count;
	}
	public String getHowToKnow() {
		return howToKnow;
	}
	public void setHowToKnow(String howToKnow) {
		this.howToKnow = howToKnow;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	

}
