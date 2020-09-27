package dfh.model.notdb;

import java.util.ArrayList;
import java.util.List;

public class AgRecordResponse {
	
	public String errorCode;
	public Integer total;
	public Integer pageSize;
	public Integer currentPage;
	
	public AgRecordResponse(){}
	
	public AgRecordResponse(String errorCode){
		this.errorCode=errorCode;
	}
	
	public AgRecordResponse(String errorCode,Integer total,Integer pageSize,Integer currentPage){
		this.errorCode=errorCode;
		this.total=total;
		this.pageSize=pageSize;
		this.currentPage=currentPage;

	}
	

	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
