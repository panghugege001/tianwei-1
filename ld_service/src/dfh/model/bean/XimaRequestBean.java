package dfh.model.bean;

import java.util.Date;
import java.util.Map;

public class XimaRequestBean {
	
	public XimaRequestBean(Date startDate, Date endDate, Map<String, Double> ximas) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.ximas = ximas;
	}

	private Date startDate;
	private Date endDate;

	private Map<String, Double> ximas;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Map<String, Double> getXimas() {
		return ximas;
	}

	public void setXimas(Map<String, Double> ximas) {
		this.ximas = ximas;
	}
	
	@Override
	public String toString() {
		return "XimaRequestBean [endDate=" + endDate + ", startDate=" + startDate + ", ximas=" + ximas + "]";
	}
	

}
