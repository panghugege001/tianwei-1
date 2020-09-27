package dfh.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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

///author:sun
public class UrlPatternFilter implements Filter {
	private static Logger log = Logger.getLogger(UrlPatternFilter.class);

	public void destroy() {
	}
	
    public static boolean sql_inj(String str)
    {
    	String upStr = str.toUpperCase();
	    String inj_str = "INSERT|SELECT|DELETE|UPDATE|SCRIPT|ALERT";
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
    
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			
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
				if(param.equals("agentcode") && StringUtils.isNotBlank(value)){
					log.info("获取到agentcode:" + value);
					session.setAttribute(Constants.AGENG_CODE, value);
				}
				if(param.equals("friendcode") && StringUtils.isNotBlank(value)){
					log.info("获取到friendcode:" + value);
					session.setAttribute("friendcode", value);
				}
				if(param.equals("agentflag") && StringUtils.isNotBlank(value)){
					log.info("获取到agentflag:" + value);
					session.setAttribute("agentflag", value);
				}
				log.info("##loginname:"+username+"##("+i+")(URL : "+req.getRequestURL() + " )(Parameter: "+param+")(Value:"+value+")##");
//				if(sql_inj(value)){
//					res.sendRedirect("/noAllowParam.jsp");
//					return ;
//				}
				i++;
			}
			
//System.out.println("UrlPatternFilter.uri:"+req.getRequestURI());
			String requestURL=req.getRequestURI().replace(req.getContextPath(), "").replace(".asp", ".jsp");
			if(requestURL.endsWith("_member.jsp")){
				if(req.getSession().getAttribute(Constants.SESSION_CUSTOMERID)==null){
					if (requestURL.indexOf("self_member.jsp")!=-1) {
						req.setAttribute("errormsg", "自助反水功能，需要登录后才能操作");
						req.getRequestDispatcher("index.jsp").forward(req, res);
					}else{
						res.sendRedirect("index.asp");
					}
					
				}else{
					req.getRequestDispatcher(requestURL).forward(req, res);
				}
				
			}else{
				req.getRequestDispatcher(requestURL).forward(req, res);
			}
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			log.error(e);
		}
	}

	public void init(FilterConfig config) throws ServletException {
	}
}