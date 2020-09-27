package dfh.action.fund;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.FundStatisticsVO;
import dfh.service.interfaces.IFundStatisticsService;
import dfh.utils.PageList;

public class FundStatistics extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 442117694105768594L;
	private Date start;
	private Date end;
	private String bankName;
	private Integer isTotal;  // 统计类型 ； 0 汇总，1 明细
	private String page;
	private PageList pageList = new PageList();
	private String errormsg;
	private IFundStatisticsService fundStatisticsServiceImpl;
	private String searchType;
	private Logger log=Logger.getLogger(FundStatistics.class);
	


	// methods:
	public String searchDetail(){
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
			FundStatisticsVO totalRecord = fundStatisticsServiceImpl.getTotalReccord(start, end, bankName,searchType);
			if (totalRecord==null) {
				this.errormsg="指定时间范围内，不存在存取款记录";
				return "error";
			}
			
			pageList.setObjectsPerPage(50);
			
			List<FundStatisticsVO> fundDetails = fundStatisticsServiceImpl.getFundDetails(start, end, bankName, pageList.getPageNumber(), pageList.getObjectsPerPage(),searchType);
			
			pageList.setFullListSize(fundDetails!=null?fundDetails.size():0);
			pageList.setList(fundDetails);
			
			this.getRequest().setAttribute("totalRecord", totalRecord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="统计各银行进出帐时，发生异常!";
			return "error";
		}
		return this.SUCCESS;
	}
	
	
	// getter and setter:
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Integer getIsTotal() {
		return isTotal;
	}
	public void setIsTotal(Integer isTotal) {
		this.isTotal = isTotal;
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
	public IFundStatisticsService getFundStatisticsServiceImpl() {
		return fundStatisticsServiceImpl;
	}
	public void setFundStatisticsServiceImpl(
			IFundStatisticsService fundStatisticsServiceImpl) {
		this.fundStatisticsServiceImpl = fundStatisticsServiceImpl;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}


}
