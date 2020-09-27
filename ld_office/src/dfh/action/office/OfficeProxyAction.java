package dfh.action.office;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dfh.action.SubActionSupport;
import dfh.model.ProxyFlow;
import dfh.service.interfaces.ProxyFlowService;

public class OfficeProxyAction extends SubActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2969122504243840537L;
	public ProxyFlowService proxyFlowService;
	public ProxyFlow proxyFlow;
	private String loginname;
	private String partner ;
	private Date start;
	private Date end;
	private Integer countOfPage;
	private Integer currentPage;
	private Integer countOfPageAll;
	private Integer currentPageAll;
	List<ProxyFlow> list = new ArrayList<ProxyFlow>();
	
	public String getProxyFlowList() {
		if (currentPage == null) {
			currentPage = 1;
		}
		if (countOfPage == null) {
			countOfPage = 10;
		}
		list = proxyFlowService.getProxyFlowList(loginname, partner, start, end, countOfPage, currentPage);
		countOfPageAll = 0;
		currentPageAll = 0;
		if (list != null && list.size() > 0 && list.get(0) != null) {
			countOfPageAll = proxyFlowService.getProxyFlowListCount(loginname, partner, start, end);
			Integer countPage = countOfPageAll / countOfPage;
			if (countOfPageAll % countOfPage > 0) {
				countPage++;
			}
			currentPageAll = countPage;
		}

		return "input";
	}
	public ProxyFlowService getProxyFlowService() {
		return proxyFlowService;
	}

	public void setProxyFlowService(ProxyFlowService proxyFlowService) {
		this.proxyFlowService = proxyFlowService;
	}

	public void setProxyFlow(ProxyFlow proxyFlow) {
		this.proxyFlow = proxyFlow;
	}

	public ProxyFlow getProxyFlow() {
		return proxyFlow;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getCountOfPage() {
		return countOfPage;
	}

	public void setCountOfPage(Integer countOfPage) {
		this.countOfPage = countOfPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<ProxyFlow> getList() {
		return list;
	}

	public void setList(List<ProxyFlow> list) {
		this.list = list;
	}
	public Integer getCountOfPageAll() {
		return countOfPageAll;
	}
	public void setCountOfPageAll(Integer countOfPageAll) {
		this.countOfPageAll = countOfPageAll;
	}
	public Integer getCurrentPageAll() {
		return currentPageAll;
	}
	public void setCurrentPageAll(Integer currentPageAll) {
		this.currentPageAll = currentPageAll;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	

}
