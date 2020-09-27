package dfh.action.office;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.nnti.office.model.auth.Operator;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import dfh.action.vo.OperatorVO;
import dfh.service.interfaces.IEmployeeManager;
import dfh.utils.Constants;
import dfh.utils.PageList;

public class EmployeeManagerAction extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055409233255321467L;
	private Map<String, Object> session;
	private IEmployeeManager employeeManager;
	private PageList pageList=new PageList();
	private String eName;
	private String rePwd;
	private String pwd;
	private String role;
	private String errormsg;
	private String isEnable;
	private String page;


	private String getRoleName(){
		Object o=session.get(Constants.SESSION_OPERATORID);
		if (o==null) {
			return null;
		}else{
			Operator operator=(Operator)o;
			return operator.getAuthority();
		}
	}
	
	
	// 查询所有
	public String findAll(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		if (page==null||page.trim().equals("")) {
			pageList.setPageNumber(1);
		}else{
			pageList.setPageNumber(Integer.parseInt(page));
		}
//		pageList.setFullListSize(employeeManager.getPageCount(manager)-1);
		pageList.setFullListSize(1000);
		pageList.setObjectsPerPage(1000);
		List eList=employeeManager.findAllEmployye(manager,pageList.getPageNumber(),pageList.getObjectsPerPage());
		if (eList==null) {
			this.errormsg=employeeManager.getErrorMessage();
		}else{
			pageList.setList(eList);
		}
		return Action.SUCCESS;
	}
	

	//指定查询 
	public String queryEmployeeById(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		if (eName==null||eName.trim().equals("")) {
			return "forwardAll";
		}else if(eName.trim().equals("admin")){
			this.errormsg="无法查询该用户";
			return Action.SUCCESS;
		}
		
		OperatorVO vo = employeeManager.findById(eName,manager);
		if (vo==null) {
			this.errormsg=employeeManager.getErrorMessage();
		}else{
			List list=new ArrayList();
			list.add(vo);
			pageList.setFullListSize(1);
			pageList.setPageNumber(1);
			pageList.setList(list);
		}
		
		return Action.SUCCESS;
	}
	
	
	// 启用、禁用员工账号
	public String changeEmployeeStatus(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		boolean execStatus = employeeManager.changeEmployeeStatus(eName, manager);
		if (!execStatus) {
			this.errormsg=employeeManager.getErrorMessage();
		}else{
			this.errormsg="操作成功";
		}
//		this.eName="";
		return Action.SUCCESS; // find all
	}
	
	
	// 删除员工账号
	public String delEmployee(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		boolean delStatus = employeeManager.delEmployee(eName, manager);
		if (!delStatus) {
			this.errormsg=employeeManager.getErrorMessage();
		}else{
			this.errormsg="操作成功";
		}
		return Action.SUCCESS; // find all
	}
	
	
	// 添加员工账号
	public String addEmployee(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		boolean execStatus=false;
		if (manager.equals("admin")) {
			execStatus=employeeManager.addEmployee(eName, pwd, manager, role);
		}else{
			execStatus=employeeManager.addEmployee(eName, pwd, manager);
		}
		if (!execStatus) {
			this.errormsg=employeeManager.getErrorMessage();
			return Action.INPUT;
		}
		return Action.SUCCESS;
	}
	

	
	

	public String getPage() {
		return page;
	}


	public void setPage(String page) {
		this.page = page;
	}
	
	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getRePwd() {
		return rePwd;
	}

	public void setRePwd(String rePwd) {
		this.rePwd = rePwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public IEmployeeManager getEmployeeManager() {
		return employeeManager;
	}

	public void setEmployeeManager(IEmployeeManager employeeManager) {
		this.employeeManager = employeeManager;
	}

	
	public String getErrormsg() {
		return errormsg;
	}
	

	public String getIsEnable() {
		return isEnable;
	}


	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public PageList getPageList() {
		return pageList;
	}


	public void setPageList(PageList pageList) {
		this.pageList = pageList;
	}


	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
	}

	
	
}
