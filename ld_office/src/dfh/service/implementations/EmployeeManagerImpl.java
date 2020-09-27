package dfh.service.implementations;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.nnti.office.model.auth.Operator;

import dfh.action.vo.OperatorVO;
import dfh.dao.LogDao;
import dfh.dao.OperatorDao;
import dfh.model.enums.OperationLogType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.IEmployeeManager;
import dfh.utils.RoleTable;

public class EmployeeManagerImpl implements IEmployeeManager {
	
	private Logger log=Logger.getLogger(EmployeeManagerImpl.class);
	private final DateFormat dateFormat=DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
	private OperatorDao operatorDao;
	private String msg;
	private LogDao logDao;

	
	
	
	public boolean addEmployee(String eName, String pwd, String manager) {
		// TODO Auto-generated method stub
		return this.addEmployee(eName, pwd, manager, RoleTable.getRole(manager));
	}

	public boolean changeEmployeeStatus(String eName,String manager) {
		// TODO Auto-generated method stub
		try {
			Object object = operatorDao.get(Operator.class, eName);
			if (object==null) {
				msg="该雇员不存在";
				return false;
			}else{
				Operator operator=(Operator) object;
				operator.setEnabled(operator.getEnabled()==0?1:0);
				operatorDao.saveOrUpdate(operator);
				logDao.insertOperationLog(manager, OperationLogType.CHENGE_EMPLOYEE_STATUS, "改变雇员状态");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("改变雇员状态或记录操作日志时发生异常", e);
			msg="系统异常，请重新操作或与管理员联系";
			return false;
		}
		return true;
	}

	public boolean delEmployee(String eName,String manager) {
		// TODO Auto-generated method stub
		try {
			Object object = operatorDao.get(Operator.class, eName);
			if (object==null) {
				msg="该雇员不存在";
				return false;
			}else{
				operatorDao.delete(Operator.class, eName);
				logDao.insertOperationLog(manager, OperationLogType.DELETE_EMPLOYEE, "删除雇员");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("删除雇员或记录操作日志时发生异常", e);
			msg="系统异常，请重新操作或与管理员联系";
			return false;
		}
		return true;
	}

	public List findAllEmployye(String manager,int pageno,int length) {
		// TODO Auto-generated method stub
		List<OperatorVO> list=null;
		try {
			int offset=(pageno-1)*length;
			List operatorList=null;
			if (manager.equals("admin")||manager.equals("boss")) {
				operatorList=operatorDao.getEmployees(offset, length);
			}else{
				String role = RoleTable.getRole(manager);
				if (role==null||role.equals("")) {
					msg="您没有查询雇员的权限，请与部门经理联系";
					return null;
				}
				operatorList=operatorDao.getEmployees(role, offset, length);
			}
			
			if (operatorList==null) {
				msg="该部门雇员人数为零";
			}else{
				list=new ArrayList<OperatorVO>();
				for (int i = 0; i < operatorList.size(); i++) {
					Operator operator=(Operator) operatorList.get(i);
					OperatorVO vo=new OperatorVO(operator.getUsername(), 
							String.valueOf(operator.getEnabled()), 
							String.valueOf(operator.getLoginTimes()), 
							operator.getLastLoginTime()!=null?this.dateFormat.format(operator.getLastLoginTime()):"", 
							operator.getLastLoginIp(), 
							operator.getCreateTime()!=null?this.dateFormat.format(operator.getCreateTime()):"",
							operator.getAuthority(),operator.getPhonenoGX(),operator.getPhonenoBL());
					list.add(vo);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询雇员信息时发生异常", e);
			msg="系统异常，请重新操作或与管理员联系";
			return null;
		}
		return list;
	}

	public OperatorVO findById(String eName,String manager) {
		// TODO Auto-generated method stub
		try {
			if (eName==null||eName.equals("")) {
				msg="要检索的雇员名称不可为空";
				return null;
			}else{
				Object object = operatorDao.get(Operator.class, eName);
				if (object==null) {
					msg="您要检索的雇员不存在，请重新输入";
					return null;
				}else{
					Operator operator=(Operator) object;
					if (!operator.getAuthority().equals(RoleTable.getRole(manager))&&!manager.equals("admin")) {
						msg="非本部门雇员;您要检索的雇员属于："+RoleTable.getDepartmentName(operator.getAuthority());
						return null;
					}else{
						
						return new OperatorVO(operator.getUsername(),
								String.valueOf(operator.getEnabled()), 
								String.valueOf(operator.getLoginTimes()), 
								dateFormat.format(operator.getLastLoginTime()), 
								operator.getLastLoginIp(), 
								dateFormat.format(operator.getCreateTime()),
								operator.getAuthority(),operator.getPhonenoGX(),operator.getPhonenoBL());
						
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("检索雇员信息时发生异常", e);
			msg="系统异常，请重新操作或与管理员联系";
			return null;
		}
	}

	public boolean addEmployee(String eName, String pwd, String manager,
			String role) {
		// TODO Auto-generated method stub
		if (role==null||role.equals("")) {
			log.error("该雇员没有权利添加新雇员");
			msg="您没有权利添加新雇员，请与部门主管取得联系";
			return false;
		}
		try {
			Object object = operatorDao.get(Operator.class, eName);
			if (object!=null) {
				msg="该账号已经存在，请更换其他雇员名称";
				return false;
			}
			Operator operator = new Operator(eName, EncryptionUtil.encryptPassword(pwd), 0, role, 0, new Timestamp(new Date().getTime()),null,null,null);
			operatorDao.save(operator);
			logDao.insertOperationLog(manager, OperationLogType.CREATE_SUB_OP, "添加新雇员："+eName);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("添加雇员或记录操作日志时发生异常", e);
			msg="系统异常，请重新操作或与管理员联系";
			return false;
		}
		
		return true;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return msg;
	}
	
	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public OperatorDao getOperatorDao() {
		return operatorDao;
	}

	public void setOperatorDao(OperatorDao operatorDao) {
		this.operatorDao = operatorDao;
	}

	public int getPageCount(String manager) {
		// TODO Auto-generated method stub
		if (manager.trim().equals("admin")) {
			return operatorDao.getCount(Operator.class);
		}else{
			return operatorDao.findByCriteria(DetachedCriteria.forClass(Operator.class).add(Restrictions.like("authority", RoleTable.getRole(manager)))).size();
		}
		
	}
		


}


