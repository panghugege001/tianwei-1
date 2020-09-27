package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.TelsivitVOForCount;
import dfh.action.vo.TelvisitVO;
import dfh.model.Telvisitremark;
import dfh.model.Telvisittask;

public interface ITelvisitService {
	
	public String getMessage();
	
	public boolean generateTask(String taskName,Date start,Date end,String isCashin,String level,String agentURL,int loginInterval,Date taskStartTime,Date taskEndTime)throws Exception;
	
	public boolean generateTaskforXima(String taskName,Date taskStartTime,Date taskEndTime)throws Exception;
	
	public List<Telvisittask> getTaskList(String taskname,int taskstatus,Date start,Date end,int pageno,int length)throws Exception;
	
	public Integer getTaskTotalCount(String taskname,int taskstatus,Date start,Date end)throws Exception;
	
	public List<TelvisitVO> getTaskDetailList(int taskid,int pageno,int length)throws Exception;
	
	public Integer getTaskDetailTotalCount(int taskid)throws Exception;
	
	public void startvisit(int visitid,String operator)throws Exception;
	
	public List<Telvisitremark> getTelvisitRemark(int telvisitid)throws Exception;
	
	public void endvisit(int visitid,int execstatus, String operator, String remark)throws Exception;
	
	public List<Telvisitremark> getTelvisitRemark(int telvisitid,int pageno,int length)throws Exception;
	
	public List<TelvisitVO> getTaskDetailList(String loginname,String phone,String email,int islock,int visitResult,Date start,Date end,int taskid,String intro, int pageno, int length)throws Exception;
	
	public Integer getTaskDetailTotalCount(String loginname,String phone,String email,int islock,int visitResult,Date start,Date end,int taskid,String intro) throws Exception;
	
	public boolean taskComplete(int taskid)throws Exception;
	
	public Telvisittask getVisitTask(int taskid)throws Exception;
	
	public String  updateTelvisittask(int taskid,String taskName,Date start,Date end)throws Exception;
	
	public String cancleTask(int taskid,String locker) throws Exception;
	
	public String unlockTelVisit(int taskid) throws Exception;
	
	public Integer getTelVisitRemarkTotalCount(String taskname,int taskstatus,Date start,Date end,String operator,int execstatus)throws Exception;
	
	public List<TelsivitVOForCount> getTelVisitRemarkTotalListForCount(String taskname,int taskstatus,Date start,Date end,String operator,int execstatus,int pageno,int length)throws Exception;
	
	public Integer getCountByOperator(int taskid);
	
	public List<TelsivitVOForCount> getCountDetailsByOperator(Telvisittask telvisittask,int pageno,int length);
}
