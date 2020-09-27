package dfh.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dfh.skydragon.webservice.UserWebServiceWS;

public class BaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected UserWebServiceWS service;
	
	public static Logger log = Logger.getLogger(BaseServlet.class);
	
	@Override
	public void init() throws ServletException {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		service = (UserWebServiceWS) ctx.getBean("userWebServiceWS");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("###############target url:" + req.getServletPath());
		//log.info("###############params:" + getParams(req));
	}
	
	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {  
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
        return ip;  
    }
	
	/**
	 * 获取所有参数
	 * @param request
	 * @return
	 */
	private String getParams(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Enumeration<String> paraNames = request.getParameterNames();
		StringBuffer strParams = new StringBuffer("");
		while (paraNames.hasMoreElements()){
			String name = paraNames.nextElement().toString();
			String value = request.getParameter(name);
			if(strParams.toString().equals("")){
				strParams.append(name).append("=").append(value);
			}else{
				strParams.append(";").append(name).append("=").append(value);
			}
		}
		return strParams.toString();
	}
}
