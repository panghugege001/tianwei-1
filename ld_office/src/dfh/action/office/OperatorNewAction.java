package dfh.action.office;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.nnti.office.model.auth.Operator;

import dfh.action.SubActionSupport;
import dfh.service.interfaces.IOperatorNewService;
import dfh.utils.Page;
import dfh.utils.PageQuery;


public class OperatorNewAction extends SubActionSupport {

	/**
	 * 对系统配置表的操作
	 */
	private static final long serialVersionUID = -1182716425426537063L;
	private Logger log = Logger.getLogger(OperatorNewAction.class);
	private IOperatorNewService operatorNewService;

	private String username;
	
	private String phonenoGX;
	
	private String phonenoBL;
	
	private String cs;
	
	private String authority;
	
	private String type;
	private String partner;
	private String agent;
	private String blServerUrl;
	private String employeeNo;
	
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	
	public String getBlServerUrl() {
		return blServerUrl;
	}
	public void setBlServerUrl(String blServerUrl) {
		this.blServerUrl = blServerUrl;
	}
	public String queryOperators(){
		DetachedCriteria dc = DetachedCriteria.forClass(Operator.class);
		if(!StringUtils.isEmpty(username)){
			dc.add(Restrictions.eq("username", username));
		}
		if(!StringUtils.isEmpty(authority)){
			dc.add(Restrictions.eq("authority", authority));
		}
		Page page = PageQuery.queryForPagenation(operatorNewService.getHibernateTemplate(), dc, null, 1000, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	/**
	 * 更新或者新增
	 * @return
	 */
	public String updateOrSaveOperator(){
		Operator operator=null;
		if(!StringUtils.isEmpty(username)){
			operator=(Operator) operatorNewService.get(Operator.class, username);
		}
		operator.setPhonenoBL(phonenoBL);
		operator.setPhonenoGX(phonenoGX);
		operator.setCs(cs);
		operator.setType(type);
		operator.setPartner(partner);
		operator.setBlServerUrl(blServerUrl);
		operator.setEmployeeNo(employeeNo);
		operator.setAgent(agent);
		if(null==operator||operator.getUsername()==null){
			operator = new Operator();
		}
		String a="";
		if(null!=operator.getUsername()){
			operatorNewService.updateSystemConfig(operator);
		}else {
			a=operatorNewService.saveSystemConfig(operator);
		}
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("操作成功##"+a);
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		
		return null;
	}
	
	public void deleteOperate(){
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			Operator operator=(Operator) operatorNewService.get(Operator.class, username);
			if(null!=operator){
				if(operator.getAuthority().equals("boss")||operator.getAuthority().equals("admin")){
					out.println("1");
					out.flush();
					return ;
				}
			}
			operatorNewService.deleteSystemConfig(Operator.class, username);
			out.println("删除成功");
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
	}
	public IOperatorNewService getOperatorNewService() {
		return operatorNewService;
	}
	public void setOperatorNewService(IOperatorNewService operatorNewService) {
		this.operatorNewService = operatorNewService;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhonenoGX() {
		return phonenoGX;
	}
	public void setPhonenoGX(String phonenoGX) {
		this.phonenoGX = phonenoGX;
	}
	public String getPhonenoBL() {
		return phonenoBL;
	}
	public void setPhonenoBL(String phonenoBL) {
		this.phonenoBL = phonenoBL;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	
}
