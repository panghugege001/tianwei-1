package dfh.action.userstatus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.UserStatusVO;
import dfh.service.interfaces.IUserstatusService;
import dfh.utils.AESUtil;
import dfh.utils.PageList;

public class UserstatusAction extends SubActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7465726190909077777L;
	private static Logger log = Logger.getLogger(UserstatusAction.class);
	private String loginname;
	private String phone;
	private String email;
	private String page;
	private Date start;
	private Date end;
	private String errormsg;
	private PageList pageList = new PageList();
	private Integer mailflag;
	private String remark;
	
	private IUserstatusService userstatusService;
	
	public String changeUserMailFlag(){
		
		try {
			if (page==null||page.trim().equals("")) {
				pageList.setPageNumber(1);
			}else{
				pageList.setPageNumber(Integer.parseInt(page));
			}
			//System.out.println(loginname+"  "+ email+"  "+ phone+"  "+  mailflag+"  "+  start+"  "+ end);
			email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			

			Integer totalCount  = userstatusService.getUserstatusCount(loginname, email, phone, mailflag, start, end);
			if (totalCount.intValue()==0) {
				this.errormsg="未找到合适纪录";
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			List<UserStatusVO> list = userstatusService.getUserstatusList(loginname, email, phone, mailflag, start, end,pageList.getPageNumber(), pageList.getObjectsPerPage());
			List<UserStatusVO> listN = new ArrayList<UserStatusVO>();
			for (UserStatusVO user : list) {
				if(StringUtils.isNotBlank(user.getEmail()) && !user.getEmail().contains("@")){
					user.setEmail(AESUtil.aesDecrypt(user.getEmail(), AESUtil.KEY));
					user.setPhone(AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY));
				} 
				listN.add(user);
			}
			
			pageList.setList(listN);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="获取纪录时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String modifyUserMailFlag(){
		try {
			String name = getRequest().getParameter("loginname");
			String flag = getRequest().getParameter("mailflag");
			userstatusService.modifyUserMailFlag(name, flag);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			this.errormsg="执行修改邮件标识位功能时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String closeTouzhuFlag(){
		try {
			try {
				this.getRequest().setCharacterEncoding("UTF-8");
				this.getResponse().setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String name = getRequest().getParameter("loginname");
			String flag = getRequest().getParameter("touzhuflag");
//			remark = getRequest().getParameter("remark");
			
			userstatusService.closeTouzhuFlag(name, flag , remark);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			this.errormsg="执行修改投注限制功能时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public PageList getPageList() {
		return pageList;
	}
	public void setPageList(PageList pageList) {
		this.pageList = pageList;
	}
	public Integer getMailflag() {
		return mailflag;
	}
	public void setMailflag(Integer mailflag) {
		this.mailflag = mailflag;
	}
	public String getPage() {
		return page;
	}
	public IUserstatusService getUserstatusService() {
		return userstatusService;
	}
	public void setUserstatusService(IUserstatusService userstatusService) {
		this.userstatusService = userstatusService;
	}


	public void setPage(String page) {
		this.page = page;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
