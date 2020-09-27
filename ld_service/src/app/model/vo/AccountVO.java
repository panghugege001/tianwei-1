package app.model.vo;

public class AccountVO {

	 /*** 代理 */
    private String agent;

    /*** 起始时间 */
    private String commissionStartDate;

    /*** 结束时间 */
    private String commissionEndDate;
    
	public Integer pageSize;
	public Integer pageIndex;

	
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getCommissionStartDate() {
		return commissionStartDate;
	}

	public void setCommissionStartDate(String commissionStartDate) {
		this.commissionStartDate = commissionStartDate;
	}

	public String getCommissionEndDate() {
		return commissionEndDate;
	}

	public void setCommissionEndDate(String commissionEndDate) {
		this.commissionEndDate = commissionEndDate;
	}
	
}