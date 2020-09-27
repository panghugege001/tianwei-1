package dfh.action.statistics;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.HowToKnowStatisticsVO;
import dfh.model.Users;
import dfh.service.implementations.HowToKnowStatisticsServiceImpl;
import dfh.service.interfaces.IHowToKnowStatisticsService;
import dfh.utils.PageList;

public class HowToKnowStatistics extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566388284394624491L;
	private Date start;
	private Date end;
	private String page;
	private PageList pageList = new PageList();
	private String errormsg;
	private String howToKnowURL;
	private Logger log=Logger.getLogger(HowToKnowStatistics.class);
	private IHowToKnowStatisticsService howToKnowStatisticsServiceImpl;
	
	

	// methods:
	public String search(){
		if (this.getOperatorLoginname()==null) {
			return "index";
		}
		
		if (start==null||end==null) {
			this.errormsg="起始日期和结束日期，都不能为空！";
			return "error";
		}
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			int recordCount = howToKnowStatisticsServiceImpl.getTotalRecordCount(start, end, howToKnowURL);
			if (recordCount==0) {
				this.errormsg="指定时间范围内，无任何记录";
				return "error";
			}
			
			pageList.setFullListSize(recordCount);
			pageList.setObjectsPerPage(50);
			List<HowToKnowStatisticsVO> userRecord = howToKnowStatisticsServiceImpl.searchUserRecord(start, end, howToKnowURL, pageList.getPageNumber(), pageList.getObjectsPerPage());
			
			pageList.setList(userRecord);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="统计来源时，发生异常! ";
			return "error";
		}
		
		return "success";
	}
	
	
	
	// getter and setter :
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
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public PageList getPageList() {
		return pageList;
	}
	public void setPageList(PageList pageList) {
		this.pageList = pageList;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	public IHowToKnowStatisticsService getHowToKnowStatisticsServiceImpl() {
		return howToKnowStatisticsServiceImpl;
	}
	public void setHowToKnowStatisticsServiceImpl(
			IHowToKnowStatisticsService howToKnowStatisticsServiceImpl) {
		this.howToKnowStatisticsServiceImpl = howToKnowStatisticsServiceImpl;
	}
	public String getHowToKnowURL() {
		return howToKnowURL;
	}
	public void setHowToKnowURL(String howToKnowURL) {
		this.howToKnowURL = howToKnowURL;
	}

}
