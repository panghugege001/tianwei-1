// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Page.java

package dfh.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

	public static final Integer PAGE_BEGIN_INDEX = 1;
	public static final Integer PAGE_DEFAULT_SIZE = 10;
	private static final long serialVersionUID = 1L;
	private Integer pageNumber;
	private Integer totalPages;
	private Integer size;
	private List pageContents;
	private Double statics1;
	private Double statics2;
	private Double statics3;
	private Integer totalRecords;
	private Integer numberOfRecordsShown;
	private String jsPageCode;

	public Page() {
		pageNumber = 0;
		totalPages = 0;
		size = 0;
		pageContents = new ArrayList();
		statics1 = 0.0;
		statics2 = 0.0;
		totalRecords = 0;
		numberOfRecordsShown = 0;
	}

	public Page(List pageContents) {
		pageNumber = 0;
		totalPages = 0;
		size = 0;
		this.pageContents = pageContents;
		statics1 = 0.0;
		statics2 = 0.0;
		totalRecords = 0;
		numberOfRecordsShown = 0;
		this.pageContents = pageContents;
	}

	public String getJsPageCode() {
		return jsPageCode;
	}

	public Integer getNumberOfRecordsShown() {
		return numberOfRecordsShown;
	}

	public List getPageContents() {
		return pageContents;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getSize() {
		return size;
	}

	public Double getStatics1() {
		return statics1;
	}

	public Double getStatics2() {
		return statics2;
	}

	public Double getStatics3() {
		return statics3;
	}

	public void setStatics3(Double statics3) {
		this.statics3 = statics3;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setJsPageCode(String jsPageCode) {
		this.jsPageCode = jsPageCode;
	}

	public void setNumberOfRecordsShown(Integer numberOfRecordsShown) {
		this.numberOfRecordsShown = numberOfRecordsShown;
	}

	public void setPageContents(List pageContents) {
		this.pageContents = pageContents;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = totalPages == null ? pageNumber : pageNumber.intValue() <= totalPages.intValue() ? pageNumber : PAGE_BEGIN_INDEX;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setStatics1(Double statics1) {
		this.statics1 = statics1;
	}

	public void setStatics2(Double statics2) {
		this.statics2 = statics2;
	}

	public void setTotalPages(Integer totalPages) {
		if (pageNumber != null)
			pageNumber = totalPages.intValue() >= pageNumber.intValue() ? pageNumber : PAGE_BEGIN_INDEX;
		this.totalPages = totalPages;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		return (new StringBuilder("Page[pageNumber:")).append(pageNumber).append(";size:").append(size).append(";totalPages:").append(totalPages).append(";pageContent:").append(
				pageContents.toString()).append("]").toString();
	}

}
