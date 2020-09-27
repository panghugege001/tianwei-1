package dfh.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

import dfh.utils.IPSeeker;

///author:sun
public class AreaRestrictFilter implements Filter {
	private static Logger log = Logger.getLogger(AreaRestrictFilter.class);

	private static List<String> allowIps = Arrays.asList(new String[] { "127.0.0.1", "112.213.99.193","58.69.130.93","152.101.44.149" });

	public void destroy() {
	}

	// private IPSeeker getIPSeeker(HttpServletRequest request) {
	// return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");
	//
	// }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		chain.doFilter(request, response);
		
//		if (allowIps.contains(request.getRemoteAddr())) {
//			chain.doFilter(request, response);
//		} else {
//			log.warn("invalid ip address:" + request.getRemoteAddr());
//			res.sendRedirect("http://www.sina.com");
//		}

		/*
		 * String forwaredFor = req.getHeader("X-Forwarded-For"); String remoteAddr = req.getRemoteAddr(); if (StringUtils.isNotEmpty(forwaredFor)) ip = forwaredFor.split(",")[0]; else { ip =
		 * remoteAddr; } log.info("Request ip:" + ip);
		 * 
		 * String[] ips = ip.replace('.', ',').split(","); String forTwo = ips[0] + "." + ips[1] + "." + "***" + "." + "***"; String forThree = ips[0] + "." + ips[1] + "." + ips[2] + "." + "***";
		 * 
		 * Map<String, String> ipconfineMap = (Map<String, String>) req.getSession().getServletContext().getAttribute("ipconfineMap"); String allow = ipconfineMap.get("allow"); String deny =
		 * ipconfineMap.get("deny"); if (deny.indexOf(ip) >= 0 || deny.indexOf(forTwo) >= 0 || deny.indexOf(forThree) >= 0) { log.info("deny ip:" + ip); res.sendRedirect("http://www.google.com/"); }
		 * else if (allow.indexOf(ip) >= 0 || allow.indexOf(forTwo) >= 0 || allow.indexOf(forThree) >= 0) { log.warn("allow ip:" + ip); chain.doFilter(request, response); } else { IPSeeker ipSeeker =
		 * getIPSeeker(req); if (ipSeeker != null) { String area = ipSeeker.getCountry(ip); if (area.indexOf("菲律宾") >= 0 || area.indexOf("澳门") >= 0 || area.indexOf("香港") >= 0 || area.indexOf("台湾") >=
		 * 0) { log.info("Deny Request ip:" + ip); res.sendRedirect("http://www.google.com/error.html"); } else { chain.doFilter(request, response); } } else {
		 * log.warn("cant find instance of ipseeker"); chain.doFilter(request, response); } }
		 */
	}

	public void init(FilterConfig config) throws ServletException {
	}
}