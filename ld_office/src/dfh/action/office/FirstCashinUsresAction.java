package dfh.action.office;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import dfh.action.vo.FirstCashinUsresVoOfTotal;
import dfh.service.interfaces.IFirstCashinUsres;
import dfh.utils.DateUtil;
import dfh.utils.PageList;

public class FirstCashinUsresAction extends ActionSupport implements
		SessionAware,ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276729536635204798L;
	private String page;
	private PageList pageList = new PageList();
	private String loginname;
	private String oneDate;
	private String twoDate;
	private IFirstCashinUsres firstCashinUsers;
	private String errormsg;
	private Map<String, Object> session;
	private String checktype;
	private String checkcontent;
	private HttpServletRequest request;
	private String dir;
	private String sort;


	public String queryUsers() {
		
		FirstCashinUsresVoOfTotal total = firstCashinUsers.getTotalPageNO(oneDate, twoDate, checktype, checkcontent);
		if (total == null) {
			this.errormsg = firstCashinUsers.getErrorMessage();
			pageList.setList(new ArrayList());
		} else {
			if (page == null || page.equals("")) {
				page = "1";
			}
			pageList.setPageNumber(Integer.parseInt(page));
			pageList.setFullListSize(total.getPersonCount());
//			pageList.setSortCriterion(sort);
//			pageList.setSortDirection(dir);
			List userList = firstCashinUsers.getUserList(oneDate, twoDate, pageList.getPageNumber(), pageList.getObjectsPerPage(), checktype, checkcontent);
			
			if (total == null) {
				this.errormsg = firstCashinUsers.getErrorMessage();
			} else {
				pageList.setList(userList);		// 得到展示数据
				request.setAttribute("total", total);
				return Action.SUCCESS;
			}

		}
		return Action.INPUT;
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

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getOneDate() {
		return oneDate;
	}

	public void setOneDate(String oneDate) {
		this.oneDate = oneDate;
	}

	public String getTwoDate() {
		return twoDate;
	}

	public void setTwoDate(String twoDate) {
		this.twoDate = twoDate;
	}


	public IFirstCashinUsres getFirstCashinUsers() {
		return firstCashinUsers;
	}

	public void setFirstCashinUsers(IFirstCashinUsres firstCashinUsers) {
		this.firstCashinUsers = firstCashinUsers;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session = arg0;
	}
	
	public String getChecktype() {
		return checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getCheckcontent() {
		return checkcontent;
	}

	public void setCheckcontent(String checkcontent) {
		this.checkcontent = checkcontent;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		request=arg0;
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	

}
