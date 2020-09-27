package dfh.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.nnti.office.model.auth.Operator;

import dfh.service.interfaces.OperatorService;
import dfh.utils.Constants;

public class MainStrutsFilter extends StrutsPrepareAndExecuteFilter {
	private static Logger log = Logger.getLogger(MainStrutsFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	HttpServletResponse resp = (HttpServletResponse) response;
        String username = null;
		HttpSession session = req.getSession();
		Operator operator = null ;
		if(null!=session.getAttribute(Constants.SESSION_OPERATORID)){
			operator = (Operator) session.getAttribute(Constants.SESSION_OPERATORID);
			username = operator.getUsername();
		}
		
		String ip = getIp(req);
		Enumeration e = req.getParameterNames();
        int i = 1;
		while(e.hasMoreElements()){
			String param = e.nextElement().toString();
			String value = req.getParameter(param);
			if(param.equals("password") || param.equals("newPassword") || param.equals("retypePassword")|| param.equals("oldPassword")){
				continue ;
			}
			log.info(ip+" ##username:"+username+"##("+i+")(Struts2URL: "+req.getRequestURL() +")(Parameter: "+param+")(Value: "+value+")##");
			i++;
		}
		if(i>1){
			log.info("\n\n");
		}
		if(null != operator){
			ServletContext sc = req.getSession().getServletContext();
			XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
			OperatorService operatorService = null ;
			if(cxt != null && cxt.getBean("operatorService") != null && operatorService == null){
				operatorService = (OperatorService) cxt.getBean("operatorService") ;
				//判断是否登录过
				Operator operatorNew = (Operator) operatorService.get(Operator.class, username);
				if(operatorNew.getEnabled() == 1 || operatorNew.getPasswordNumber()>=5){
					session.setAttribute(Constants.SESSION_OPERATORID, null);
					operatorService.insertExceptionLog(operator, "密码错误5次或被冻结自动退出系统"+ip);
					log.warn(username+"错误密码超过5次，自动登出。ip:"+ip);
				}
				if(!operator.getRandomStr().equals(operatorNew.getRandomStr())){
					session.setAttribute(Constants.SESSION_OPERATORID, null);
//					operatorService.insertExceptionLog(operator, "同时登录异常"+ip);
					log.warn(username+"有异常登录情况"+ip);
				 }
			}
		}
        super.doFilter(request, response, chain);
    }
    
    
    public String getIp(HttpServletRequest request) {  
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
        if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;
    }
    
    public void writeJson(String json , HttpServletResponse resp){
        try {
        	resp.setContentType("json/application;charset=UTF-8");
        	resp.setHeader("Charset", "UTF-8");
        	resp.setHeader("Cache-Control", "no-cache");
			PrintWriter out = resp.getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}