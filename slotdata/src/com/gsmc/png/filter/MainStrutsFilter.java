package com.gsmc.png.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;


public class MainStrutsFilter extends StrutsPrepareAndExecuteFilter {
	private static Logger log = Logger.getLogger(MainStrutsFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest req = (HttpServletRequest) request;
		
		/*String ip = getIp(req);
		log.info("ACCESS IP IS:" + ip);
		Enumeration e = req.getParameterNames();
        int i = 1;
		while(e.hasMoreElements()){
			String param = e.nextElement().toString();
			String value = req.getParameter(param);
			if(param.equals("password") || param.equals("newPassword") || param.equals("retypePassword")|| param.equals("oldPassword")){
				continue ;
			}
			log.info(ip+" ##("+i+")(Struts2URL: "+req.getRequestURL() +")(Parameter: "+param+")(Value: "+value+")##");
			i++;
		}
		if(i>1){
			log.info("\n\n");
		}*/
        
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