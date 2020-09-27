package dfh.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Operator;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class SubActionSupport extends ActionSupport {
	
	private Logger log=Logger.getLogger(SubActionSupport.class);
	
	protected ObjectMapper JSON = new ObjectMapper();

	public String getCustomerLoginname()throws Exception {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user != null)
				return user.getLoginname();
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}

	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	public String getIp() {
		HttpServletRequest request = getRequest() ;
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        log.debug("getIpAddr-->"+ip);
        if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;  
	}
	
	public String getIpAddr() {  
		HttpServletRequest request = getRequest() ;
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        log.debug("getIpAddr-->"+ip);
        if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;  
    }  

	public String getOperatorLoginname() throws Exception{
		try {
			Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
			if (operator != null)
				return operator.getUsername();
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}

	public String getPartnerLoginname() throws Exception {
		try {
			Users partner = (Users) getHttpSession().getAttribute(Constants.SESSION_PARTNERID);
			if (partner != null)
				return partner.getLoginname();
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
		return response;
	}

	public Users getCustomerFromSession() throws Exception{
		try {
			return (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}
	
	public void refreshUserInSession() throws Exception {
		Users customer = getCustomerFromSession();
		Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customer.getLoginname() }, Users.class);
		getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
		if(customer.getPostcode()==null || user.getPostcode()==null){
			 getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		 }
		 if(!customer.getPostcode().equals(user.getPostcode())){
			 getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		 }
	}
	
	public void writeJson(String json){
        try {
        	getResponse().setContentType("json/application;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("["+SubActionSupport.class.getName()+"]",e);
		}
    }
	
	public void writeText(String text){
        try {
        	getResponse().setContentType("text/plain;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(text);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("["+SubActionSupport.class.getName()+"]",e);
		}
    }
	
	//获取域名
	protected String getHost(){
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer url = request.getRequestURL();  
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();  
		return tempContextUrl;
	}
	
}
