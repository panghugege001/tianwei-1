package dfh.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.apache.struts2.ServletActionContext;

import dfh.model.Users;
import dfh.utils.Constants;
import dfh.utils.IPSeeker;
import dfh.utils.ThreadPoolUtil;

///author:sun
public class WebRestrictFilter implements Filter {
	private static Logger log = Logger.getLogger(WebRestrictFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String requestURL = req.getRequestURL().toString();
		System.out.println(requestURL);
		String requestName = requestURL.substring(requestURL.lastIndexOf("/") + 1);
		if (requestName.contains("webconsole")) {
			res.sendRedirect("/");
			return;
		}
		System.out.println(requestName);
		chain.doFilter(request, response);
	}

	/**
	 * 判断是不是ip
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		boolean ipAddress = mat.find();
		return ipAddress;
	}

	public void init(FilterConfig config) throws ServletException {
	}
}