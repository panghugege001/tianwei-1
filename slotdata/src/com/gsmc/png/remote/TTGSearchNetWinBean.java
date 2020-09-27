package com.gsmc.png.remote;

import java.util.ArrayList;
import java.util.List;

public class TTGSearchNetWinBean {
	
	private Integer totalRecords;
	private Integer pageSize;
	private String more;
	private String startIndexKey;
	private String requestId;
	private List<TTGNetWinBean> ttgNetWinBeanList = new ArrayList<TTGNetWinBean>();
	
	public TTGSearchNetWinBean() {
		
	}
	
	public TTGSearchNetWinBean(Integer totalRecords, Integer pageSize, String more, String startIndexKey,
			String requestId) {
		super();
		this.totalRecords = totalRecords;
		this.pageSize = pageSize;
		this.more = more;
		this.startIndexKey = startIndexKey;
		this.requestId = requestId;
	}
	
	public boolean isMore() {
		return "Y".equals(more);
	}
	
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
	public String getStartIndexKey() {
		return startIndexKey;
	}
	public void setStartIndexKey(String startIndexKey) {
		this.startIndexKey = startIndexKey;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public List<TTGNetWinBean> getTtgNetWinBeanList() {
		return ttgNetWinBeanList;
	}

	public void setTtgNetWinBeanList(List<TTGNetWinBean> ttgNetWinBeanList) {
		this.ttgNetWinBeanList = ttgNetWinBeanList;
	}

	@Override
	public String toString() {
		return "TTGSearchNetWinBean [totalRecords=" + totalRecords + ", pageSize=" + pageSize + ", more=" + more
				+ ", startIndexKey=" + startIndexKey + ", requestId=" + requestId + ", ttgNetWinBeanList="
				+ ttgNetWinBeanList + "]";
	}
}
