package dfh.action.customer;

import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.TaskAmount;
import dfh.model.TaskList;
import dfh.model.TaskUserRecord;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.Page;

public class YaoYaoAction extends SubActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(YaoYaoAction.class);
	
	private String starttime ;
	private String endtime ;
	private Integer pageIndex ;
	private Integer size ;
	private String errormsg ;
	
	private Integer taskId ; 
	private Double amount;

	//获取任务列表
	public String getTaskList(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(!user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			GsonUtil.GsonObject("只允许真钱玩家参与该活动");
			return null;
		}
		try {
			List<TaskList> tasks = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "getAllTasks", new Object[] {user.getLoginname()}, TaskList.class);
			GsonUtil.GsonObject(tasks);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(e.getMessage());
		}
		return null ;
	}
	
	//保存玩家任务
	public String saveUserTask(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(!user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			GsonUtil.GsonObject("只允许真钱玩家参与该活动");
			return null;
		}
		if(null == taskId){
			GsonUtil.GsonObject("请输入任务ID");
			return null;
		}
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
							"saveTask", new Object[] { user.getLoginname() , taskId}, String.class);
			GsonUtil.GsonObject(result);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(e.getMessage());
		}
		return null ;
	}
	
	//获取累计金额
	public String getTaskAmount(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(!user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			GsonUtil.GsonObject("只允许真钱玩家参与该活动");
			return null;
		}
		try {
			String updateStr = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"updateUserTask", new Object[] { user.getLoginname()}, String.class);
			log.info("updateUserTask-->"+user.getLoginname()+" "+updateStr);
			
			TaskAmount taskAmount = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
							"getTaskAmount", new Object[] { user.getLoginname()}, TaskAmount.class);
			GsonUtil.GsonObject(taskAmount.getAmount());
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("获取错误");
		}
		return null ;
	}
	
	public String transferTaskAmount(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(!user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			GsonUtil.GsonObject("只允许真钱玩家参与该活动");
			return null;
		}
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"receiveTaskAmount", new Object[] { user.getLoginname() , amount}, String.class);
			GsonUtil.GsonObject(result);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("转入失败："+e.getMessage());
		}
		return null ;
	}
	
	public String queryTaskRecords() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				setErrormsg("请您从首页登录");
				return "index";
			}
			String updateStr = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"updateUserTask", new Object[] { user.getLoginname() }, String.class);
			log.info("updateUserTask-->" + user.getLoginname() + " " + updateStr);

			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "queryTaskRecord", new Object[] { getCustomerLoginname(), pageIndex , size}, Page.class, TaskUserRecord.class);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}
	
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
}
