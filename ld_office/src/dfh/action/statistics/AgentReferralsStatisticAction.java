package dfh.action.statistics;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.AgentReferralsVO;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.IAgentReferralsStatistic;
import dfh.utils.Constants;
import dfh.utils.PageList;

public class AgentReferralsStatisticAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3352887666211973200L;
//	private PageList pageList=new PageList();
//	private String page;
	private Date start;
	private Date end;
	private String errormsg;
	private Logger log=Logger.getLogger(AgentReferralsStatisticAction.class);
	private IAgentReferralsStatistic agentReferralsStatistic;
	private String agentFlag;
	private int flagType;
	
	
	public String search(){
		if (this.getOperatorLoginname()==null) {
			return "index";
		}
		
		if (start==null||end==null) {
			this.errormsg="起始日期和结束日期，都不能为空！";
			return INPUT;
		}
		
		try {
			Users user = agentReferralsStatistic.getUser(agentFlag, flagType);
			if (user==null) {
				errormsg="查询的对象不存在，请重新输入";
				return INPUT;
			}else{
				if (!user.getRole().equals(UserRole.AGENT.getCode())) {
					errormsg="非代理账号";
					return INPUT;
				}else if (user.getFlag().intValue()!=Constants.ENABLE.intValue()) {
					errormsg="该用户已被禁用";
					return INPUT;
				}else{
					AgentReferralsVO total = agentReferralsStatistic.getAgentReferralsCount(user.getLoginname(), start, end);
					if (total==null) {
						this.errormsg="指定时间内，该代理没有存款的下线会员";
						return INPUT;
					}else{
						
						this.getRequest().setAttribute("personCount", total.getPersonCount());
						this.getRequest().setAttribute("amount", total.getAmount());
					}
					return SUCCESS;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("统计代理下线时，发生异常",e);
			this.errormsg="系统异常:"+e.getMessage().toString();
			return "error";
		}
		
	}

/*
	public String search(){
		if (this.getOperatorLoginname()==null) {
			return "index";
		}
		
		if (start==null||end==null) {
			this.errormsg="起始日期和结束日期，都不能为空！";
			return INPUT;
		}
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			Users user = agentReferralsStatistic.getUser(agentFlag, flagType);
			if (user==null) {
				errormsg="查询的目标不存在，请重新输入";
				return INPUT;
			}else{
				if (user.getFlag().intValue()!=Constants.ENABLE.intValue()) {
					errormsg="该用户已被禁用";
					return INPUT;
				}else{
					AgentReferralsVO total = agentReferralsStatistic.getAgentReferralsCount(user.getLoginname(), start, end);
					if (total==null) {
						this.errormsg="该代理没有下线会员";
						return INPUT;
					}else{
						pageList.setFullListSize(total.getPersonCount());
						pageList.setObjectsPerPage(10);
						
						List<AgentReferralsVO> userList = agentReferralsStatistic.getAgentReferralsList(user.getLoginname(), start, end, pageList.getPageNumber(), pageList.getObjectsPerPage());
						pageList.setList(userList);
						this.getRequest().setAttribute("personCount", total.getPersonCount());
						this.getRequest().setAttribute("amount", total.getAmount());
					}
					return SUCCESS;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("统计代理下线时，发生异常",e);
			this.errormsg="系统异常:"+e.getMessage().toString();
			return "error";
		}
		
	}
	
	*/

//	public PageList getPageList() {
//		return pageList;
//	}
//	public void setPageList(PageList pageList) {
//		this.pageList = pageList;
//	}
//	public String getPage() {
//		return page;
//	}
//	public void setPage(String page) {
//		this.page = page;
//	}
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
	
	public IAgentReferralsStatistic getAgentReferralsStatistic() {
		return agentReferralsStatistic;
	}



	public void setAgentReferralsStatistic(
			IAgentReferralsStatistic agentReferralsStatistic) {
		this.agentReferralsStatistic = agentReferralsStatistic;
	}
	public String getAgentFlag() {
		return agentFlag;
	}



	public void setAgentFlag(String agentFlag) {
		this.agentFlag = agentFlag;
	}



	public int getFlagType() {
		return flagType;
	}



	public void setFlagType(int flagType) {
		this.flagType = flagType;
	}


}
