package dfh.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.Users;
import dfh.utils.Constants;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;


public class MainStrutsFilter extends StrutsPrepareAndExecuteFilter {
	private static Logger log = Logger.getLogger(MainStrutsFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	HttpServletResponse resp = (HttpServletResponse) response;
    	String username = null;
		HttpSession session = req.getSession();
		if(null!=session.getAttribute(Constants.SESSION_CUSTOMERID)){
			Users user = (Users) session.getAttribute(Constants.SESSION_CUSTOMERID);
			username = user.getLoginname();
		}
    	
        Enumeration e = req.getParameterNames();
        int i = 1;
		while(e.hasMoreElements()){
			String param = e.nextElement().toString();
			String value = req.getParameter(param);
			log.info("##loginname:"+username+"##("+i+")(Struts2URL: "+req.getRequestURL() +")(Parameter: "+param+")(Value: "+value+")##");
			if(sql_inj(value)){
				log.warn("被攻击！IP:" + getIp(req));
				resp.sendRedirect("/noAllowParam.jsp");
				return ;
			}
			i++;
		}
        
        super.doFilter(request, response, chain);
        
    }
    public static boolean sql_inj(String str)
    {
    	String upStr = str.toUpperCase();
		String inj_str = "INSERT|SELECT|DELETE|UPDATE|WGET|COMMAND|JAVASCRIPT|-RM|EDITPROPERTY|FILELIST|FOLDER|PACKEDFILE|CDN|<%@|<%";
	    String inj_stra[] = inj_str.split("\\|");
	    for (int i=0 ; i < inj_stra.length ; i++ )
	    {
		    if (upStr.indexOf(inj_stra[i])>=0)
		    {
		     return true;
		    }
	    }
	    return false;
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
		log.debug("getIpAddr-->"+ip);
		if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;
	}
    public static void main(String[] args) {
    	System.out.println(sql_inj(""));;
	}
}