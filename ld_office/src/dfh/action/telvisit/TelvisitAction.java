package dfh.action.telvisit;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.action.vo.TelsivitVOForCount;
import dfh.action.vo.TelvisitVO;
import dfh.model.Telvisitremark;
import dfh.model.Telvisittask;
import dfh.service.interfaces.ITelvisitService;
import dfh.utils.PageList;

public class TelvisitAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7913911349353899920L;
	private String taskName,agentURL,isCashin,level,page,loginInterval,loginname,visitresult,visitid,email,phone,flag,intro;
	private Date start,end,taskStartTime,taskEndTime;
	private int taskid,execstatus=-1,islock=-1;
	private ITelvisitService telvisitService;
	private Logger log=Logger.getLogger(TelvisitAction.class);
	private String errormsg;
	private PageList pageList = new PageList();
	


	// methods:
	
	public String visitComplete(){
		PrintWriter out=null;
		try {
			out = this.getResponse().getWriter();
			if (telvisitService.taskComplete(taskid)) {
				out.println("任务完成");
			}else{
				out.println("存在未回访客户，请检查");
			}
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="执行[完成任务]时，发生异常："+e.getMessage().toString();
			return ERROR;
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
		return null;
	}
	
	public String getRemarks(){
		try {
			if (page==null||page.trim().equals("")) {
				pageList.setPageNumber(1);
			}else{
				pageList.setPageNumber(Integer.parseInt(page));
			}
			
			List<Telvisitremark> telvisitRemarks =telvisitService.getTelvisitRemark(Integer.parseInt(visitid));
			if (telvisitRemarks==null||telvisitRemarks.size()<=0) {
				pageList.setFullListSize(0);
				return SUCCESS;
			}
			pageList.setFullListSize(telvisitRemarks.size());

			telvisitRemarks=telvisitService.getTelvisitRemark(Integer.parseInt(visitid), pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(telvisitRemarks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="查询回访明细时，发生异常:"+e.getMessage().toString();
		}
		return SUCCESS;
	}
	
	public String visitResult(){
		try {
			telvisitService.endvisit(Integer.parseInt(visitid), execstatus, this.getOperatorLoginname(), visitresult);
			execstatus=-1;
			searchVisits();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="添加回访结果时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String endvisit(){
		try {
			List<Telvisitremark> telvisitRemarks = telvisitService.getTelvisitRemark(Integer.parseInt(visitid));
			this.getRequest().setAttribute("remarks", telvisitRemarks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="结束访问时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String startvisit(){
		try {
			telvisitService.startvisit(Integer.parseInt(visitid), this.getOperatorLoginname());
			searchVisits();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="执行[开始访问]功能时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String searchVisits(){
		
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			Integer totalCount = telvisitService.getTaskDetailTotalCount(loginname, phone, email, islock, execstatus, start, end, taskid,intro);
			if (totalCount==null||totalCount.intValue()==0) {
				pageList.setFullListSize(0);
			}else{
				pageList.setFullListSize(totalCount.intValue());
				pageList.setObjectsPerPage(50);
				
				List<TelvisitVO> detailList = telvisitService.getTaskDetailList(loginname, phone, email, islock, execstatus, start, end, taskid,intro, pageList.getPageNumber(), pageList.getObjectsPerPage());
				pageList.setList(detailList);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="查询回访用户时，发生异常："+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getVisitTaskAll(){
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
//			taskName=new String(taskName.getBytes("iso-8859-1"),"utf-8");
			
			Telvisittask visitTask = telvisitService.getVisitTask(taskid);
			if (visitTask==null) {
				this.errormsg="请不要篡改数据";
				return ERROR;
			}
			if (visitTask.getStarttime().after(new Date())) {
				this.errormsg="该任务尚未开始";
				return ERROR;
			}
			
			Integer totalCount = telvisitService.getTaskDetailTotalCount(taskid);
			if (totalCount.intValue()==0) {
				this.errormsg="任务列表为空";
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			List<TelvisitVO> users = telvisitService.getTaskDetailList(taskid, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="获取任务明细时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getVisitTaskAllForUnlock(){
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
//			taskName=new String(taskName.getBytes("iso-8859-1"),"utf-8");
			
			Telvisittask visitTask = telvisitService.getVisitTask(taskid);
			if (visitTask==null) {
				this.errormsg="请不要篡改数据";
				return ERROR;
			}
			
			Integer totalCount = telvisitService.getTaskDetailTotalCount(taskid);
			if (totalCount.intValue()==0) {
				this.errormsg="任务列表为空";
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			List<TelvisitVO> users = telvisitService.getTaskDetailList(taskid, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="获取任务明细时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getVisitTaskAllForNoUnlock(){
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
//			taskName=new String(taskName.getBytes("iso-8859-1"),"utf-8");
			
			Telvisittask visitTask = telvisitService.getVisitTask(taskid);
			if (visitTask==null) {
				this.errormsg="请不要篡改数据";
				return ERROR;
			}
			
			Integer totalCount = telvisitService.getTaskDetailTotalCount(taskid);
			if (totalCount.intValue()==0) {
				this.errormsg="任务列表为空";
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			List<TelvisitVO> users = telvisitService.getTaskDetailList(taskid, pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="获取任务明细时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/*
	 * 对同一个项目，对客服的回访量进行统计，并按照多少排序
	 */
	public String getCountNumberByOperator(){
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		try {
//			taskName=new String(taskName.getBytes("iso-8859-1"),"utf-8");
			
			Telvisittask visitTask = telvisitService.getVisitTask(taskid);
			if (visitTask==null) {
				this.errormsg="请不要篡改数据";
				return ERROR;
			}
			
			Integer totalCount = telvisitService.getCountByOperator(taskid);
			if (totalCount.intValue()==0) {
				this.errormsg="任务列表为空";
				return ERROR;
			}
			pageList.setFullListSize(totalCount);
			pageList.setObjectsPerPage(50);
			//List<TelvisitVO> users = telvisitService.getTaskDetailList(taskid, pageList.getPageNumber(), pageList.getObjectsPerPage());
			List<TelsivitVOForCount> telsivitVOForCounts =telvisitService.getCountDetailsByOperator(visitTask,pageList.getPageNumber(), pageList.getObjectsPerPage());
			pageList.setList(telsivitVOForCounts);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="获取任务明细时，发生异常:"+e.getMessage().toString();
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	
	/*
	 * 解锁专用
	 */
	public String unlockTelVisit(){
		
		
		try {
			String id = getRequest().getParameter("id");
			
			String msg = telvisitService.unlockTelVisit(Integer.parseInt(id));

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			this.errormsg="执行[解除锁定]功能时，发生异常:"+e.getMessage().toString();
			return ERROR;
			
		}
//		return INPUT;
		return SUCCESS;
	
	}

	public String generateTask(){
		try {
			if (taskName==null||taskName.trim().equals("")) {
				this.errormsg="任务名称不可以为空";
				return ERROR;
			}
			int flagNum=1;//是否生成系统洗码回访的标识
			if (flag!=null&&!flag.trim().equals("")) {
				flagNum = Integer.parseInt(flag);
			}
			boolean generateTaskStatus = false;
			if(flagNum ==1){
				int intervalNum=0;
				if(loginInterval!=null&&!loginInterval.trim().equals(""))
					intervalNum=Integer.parseInt(loginInterval);
				generateTaskStatus = telvisitService.generateTask(taskName, start, end, isCashin,level, agentURL, intervalNum, taskStartTime, taskEndTime);
				
			}else {
				generateTaskStatus = telvisitService.generateTaskforXima(taskName, taskStartTime, taskEndTime);
			}
			if (!generateTaskStatus) {
				this.errormsg=telvisitService.getMessage();
				return ERROR;
			}else{
				this.errormsg="生成任务列表成功";
				return SUCCESS;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.errormsg="生成任务列表发生异常，请稍后重试。"+e.getMessage().toString();
			return ERROR;
		}
		
	}
	
	
	
	
	// setter and getter:
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getAgentURL() {
		return agentURL;
	}
	public void setAgentURL(String agentURL) {
		this.agentURL = agentURL;
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
	public Date getTaskStartTime() {
		return taskStartTime;
	}
	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
	}
	public Date getTaskEndTime() {
		return taskEndTime;
	}
	public void setTaskEndTime(Date taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	
	public ITelvisitService getTelvisitService() {
		return telvisitService;
	}
	public void setTelvisitService(ITelvisitService telvisitService) {
		this.telvisitService = telvisitService;
	}
	public String getIsCashin() {
		return isCashin;
	}


	public void setIsCashin(String isCashin) {
		this.isCashin = isCashin;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public String getPage() {
		return page;
	}


	public void setPage(String page) {
		this.page = page;
	}


	public int getTaskid() {
		return taskid;
	}


	public void setTaskid(int taskid) {
		this.taskid = taskid;
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
	public String getVisitresult() {
		return visitresult;
	}

	public void setVisitresult(String visitresult) {
		this.visitresult = visitresult;
	}
	public String getLoginInterval() {
		return loginInterval;
	}

	public void setLoginInterval(String loginInterval) {
		this.loginInterval = loginInterval;
	}

	public int getExecstatus() {
		return execstatus;
	}

	public void setExecstatus(int execstatus) {
		this.execstatus = execstatus;
	}
	public String getVisitid() {
		return visitid;
	}

	public void setVisitid(String visitid) {
		this.visitid = visitid;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIslock() {
		return islock;
	}

	public void setIslock(int islock) {
		this.islock = islock;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	

}
