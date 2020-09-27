package dfh.utils;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class PageList implements PaginatedList {
	
	/** 
     * 每页的列表
      */ 
     private  List list;

     /**
     * 当前页码
      */ 
     private   int  pageNumber  =   1 ;

     /**
     * 每页记录数 page size
      */ 
     private   int  objectsPerPage  =   20 ;

     /** 
     * 总记录数
      */ 
     private   int  fullListSize  =   0 ;

     private  String sortCriterion;

     private  SortOrderEnum sortDirection=SortOrderEnum.DESCENDING;
     
     private  String searchId;
     
     

     public void setList(List list) {
		this.list = list;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setObjectsPerPage(int objectsPerPage) {
		this.objectsPerPage = objectsPerPage;
	}

	public void setFullListSize(int fullListSize) {
		this.fullListSize = fullListSize;
	}

	public void setSortCriterion(String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public void setSortDirection(SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	public void setSortDirection(String sortDirection) {
		if(sortDirection!=null&&!sortDirection.equals("")){
			if ("desc".equals(sortDirection)) {
				this.sortDirection=SortOrderEnum.DESCENDING;
			}
		}
		this.sortDirection = SortOrderEnum.ASCENDING;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}


	public int getFullListSize() {
		// TODO Auto-generated method stub
		return this.fullListSize;
	}

	public List getList() {
		// TODO Auto-generated method stub
		return this.list;
	}

	public int getObjectsPerPage() {
		// TODO Auto-generated method stub
		return this.objectsPerPage;
	}

	public int getPageNumber() {
		// TODO Auto-generated method stub
		return this.pageNumber;
	}

	public String getSearchId() {
		// TODO Auto-generated method stub
		return this.searchId;
	}

	public String getSortCriterion() {
		// TODO Auto-generated method stub
		return this.sortCriterion;
	}

	public SortOrderEnum getSortDirection() {
		// TODO Auto-generated method stub
		return this.sortDirection;
	}

}
