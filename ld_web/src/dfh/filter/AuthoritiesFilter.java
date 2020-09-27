package dfh.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.Operator;
import dfh.utils.Constants;
import dfh.utils.StringUtil;

public class AuthoritiesFilter implements Filter {
	private static Logger log = Logger.getLogger(AuthoritiesFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
//System.out.println("AuthoritiesFilter.uri:"+req.getRequestURI());
			if(req.getRequestURI().endsWith("_member.jsp")){
				if(req.getSession().getAttribute(Constants.SESSION_CUSTOMERID)==null){
					if (req.getRequestURI().indexOf("self_member.jsp")!=-1) {
						req.setAttribute("errormsg", "自助反水功能，需要登录后才能操作");
						req.getRequestDispatcher("index.asp").forward(req, res);
					}else{
						res.sendRedirect("index.asp");
					}
					
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			log.error(e);
		}
	}

	public void init(FilterConfig config) throws ServletException {
	}
}