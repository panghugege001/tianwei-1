package dfh.action.customer;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class AuthorityInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4781781030541307311L;

	protected String doIntercept(ActionInvocation arg0) {
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			Users customer = (Users) session.getAttribute(Constants.SESSION_CUSTOMERID);
			if(customer!=null){
				String timeKey = "timeKey" ;
				Date oldTime = (Date) session.getAttribute(timeKey) ;
				if(null==oldTime){
					oldTime = new Date();
					session.setAttribute(timeKey, oldTime );
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间  
		        String nowtime = dateFormat.format(new Date());// 按以上格式 将当前时间转换成字符串  
		        String testtime = dateFormat.format(session.getAttribute(timeKey));// 测试时间  
		        long result = 1;
		        try {  
		            result = (dateFormat.parse(nowtime).getTime() - dateFormat.parse(testtime).getTime()) / 1000; // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时  
		            //log.info("当前时间减去测试时间=" + result + "秒");
		        } catch (Exception e) {  
		            e.printStackTrace();
		        }  
				if(result>180){
					 Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
								+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {
						 customer.getLoginname()}, Users.class);
						 if(user==null || user.getFlag()==1){
							 session.setAttribute(Constants.SESSION_CUSTOMERID, null);
									return Action.ERROR;
						 }
						 if(customer.getPostcode()==null || user.getPostcode()==null){
							 session.setAttribute(Constants.SESSION_CUSTOMERID, null);
								return Action.ERROR;
						 }
						 if(!customer.getPostcode().equals(user.getPostcode())){
							 session.setAttribute(Constants.SESSION_CUSTOMERID, null);
								return Action.ERROR;
						 }
						 user.setLastLoginTime(new Date());
						// log.info("***********刷新数据********"+customer.getLoginname());
					     session.setAttribute(Constants.SESSION_CUSTOMERID, user);
					    oldTime = new Date();
						session.setAttribute(timeKey, oldTime );
				}else{
					//log.info("***********不刷新数据********"+customer.getLoginname());
				}
			}
			return arg0.invoke();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Action.ERROR;
		}
	}

}
