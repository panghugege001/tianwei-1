package dfh.model.bean;

import java.util.ArrayList;
import java.util.List;

import dfh.model.Suggestion;

public class SuggestionVo {

	private Integer count;
	private Integer pageNo;
	private Integer pageSize;
	private List<Suggestion> sugList = new ArrayList<Suggestion>();
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public List<Suggestion> getSugList() {
		return sugList;
	}
	public void setSugList(List<Suggestion> sugList) {
		this.sugList = sugList;
	}
	
}
