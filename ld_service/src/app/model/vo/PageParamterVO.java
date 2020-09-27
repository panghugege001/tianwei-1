package app.model.vo;

/**
 * 接收请求参数，有分页的查询可共用，查询条件继承该类即可
 * @author lin
 *
 */
public  class PageParamterVO {
    public  Integer pageSize;
    public  Integer pageIndex;
    public  String  loginName;
    
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
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
