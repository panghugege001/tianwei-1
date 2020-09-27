package dfh.action.telvisit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.TelsivitVOForCount;
import dfh.model.Telvisittask;
import dfh.service.interfaces.ITelvisitService;
import dfh.utils.DateUtil;
import dfh.utils.PageList;

public class TelvisittaskAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6415220763178430627L;
	private Date start,end;
	private int taskstatus,execstatus;
	private PageList pageList = new PageList();
	private String errormsg,taskname,page,operator;
	private Logger log=Logger.getLogger(TelvisittaskAction.class);
	private ITelvisitService telvisitService;
	
	


	// methods:
	public String getTaskList(){
		
		if (start==null||end==null) {
			this.errormsg="起始时间和结束时间，不可以为空";
			return ERROR;
		}
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			Integer totalCount = telvisitService.getTaskTotalCount(taskname, taskstatus, start, end);
			if (totalCount==null) {
				this.errormsg=telvisitService.getMessage();
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			
			List<Telvisittask> taskList = telvisitService.getTaskList(taskname, taskstatus, start, end, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(taskList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="查询任务列表时，发生异常，请稍后再试";
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	/*
	 * 统计结果用
	 */
	public String getTelTaskCount() {
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			Integer totalCount = telvisitService.getTelVisitRemarkTotalCount(taskname, taskstatus, start, end,operator,execstatus);
			if (totalCount==null) {
				this.errormsg=telvisitService.getMessage();
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			List<TelsivitVOForCount> taskList = telvisitService.getTelVisitRemarkTotalListForCount(taskname, taskstatus, start, end, operator, execstatus, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(taskList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="查询任务列表时，发生异常，请稍后再试";
			return ERROR;
		}
		
		return INPUT;
	}
	
	// methods:
	public String getTaskListForManage(){
		
		if (start==null||end==null) {
			this.errormsg="起始时间和结束时间，不可以为空";
			return ERROR;
		}
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			Integer totalCount = telvisitService.getTaskTotalCount(taskname, taskstatus, start, end);
			if (totalCount==null) {
				this.errormsg=telvisitService.getMessage();
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			
			List<Telvisittask> taskList = telvisitService.getTaskList(taskname, taskstatus, start, end, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(taskList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="查询任务列表时，发生异常，请稍后再试";
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String modifyTelVisitTask(){
		
		String taskid = getRequest().getParameter("taskid");
		
		Telvisittask telvisittask;
		try {
			telvisittask = telvisitService.getVisitTask(Integer.parseInt(taskid));
			getRequest().setAttribute("telvisittask", telvisittask);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.errormsg="查询任务时，发生异常，请稍后再试";
			return ERROR;
		}
		return "input";
		
	}
	/*
	 * 执行更新操作
	 */
	public String updateTask(){
		
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out=null;
		try {
			out=this.getResponse().getWriter();
			String id = getRequest().getParameter("id");
			Telvisittask telvisittask = telvisitService.getVisitTask(Integer.parseInt(id));
			String taskNewName = (getRequest().getParameter("taskname"));
			Date startNewTime = DateUtil.parseDateForStandard(getRequest().getParameter("start"));
			Date endNewTime = DateUtil.parseDateForStandard(getRequest().getParameter("end"));
			String message = telvisitService.updateTelvisittask(Integer.parseInt(id), taskNewName, startNewTime, endNewTime);
			
			if("" == message){
				out.println("保存成功");
			}else{
				out.println(message);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println("执行失败:");
		}
		return null;
	}
	
	/*
	 * 执行取消操作
	 */
	public String cancleTelVisitTask(){
		
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out=null;
		
		try {
			String id = getRequest().getParameter("id");
			
			String msg = telvisitService.cancleTask(Integer.parseInt(id),getCustomerLoginname());
			//String msg = null;
			out=this.getResponse().getWriter();
			if (msg == null)
				out.println("取消成功");
			else
				out.println("取消失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("取消失败:" + e.getMessage());
			out.flush();
		}
		finally{
			if(out!=null){
				out.close();
			}
		}
//		return INPUT;
		return null;
		
		//return INPUT;
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
	public int getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(int taskstatus) {
		this.taskstatus = taskstatus;
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
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}


	public void setPage(String page) {
		this.page = page;
	}


	public String getPage() {
		return page;
	}


	public ITelvisitService getTelvisitService() {
		return telvisitService;
	}


	public void setTelvisitService(ITelvisitService telvisitService) {
		this.telvisitService = telvisitService;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public int getExecstatus() {
		return execstatus;
	}


	public void setExecstatus(int execstatus) {
		this.execstatus = execstatus;
	}

	
	
}
