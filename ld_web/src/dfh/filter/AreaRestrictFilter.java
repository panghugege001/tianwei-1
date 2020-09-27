package dfh.filter;

import java.io.IOException;
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
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import app.enums.CacheEnums;
import app.util.RedisUtil;
import dfh.utils.IPSeeker;

public class AreaRestrictFilter implements Filter {
	
	private static Logger log = Logger.getLogger(AreaRestrictFilter.class);

	public void destroy() {
	
	}

	private IPSeeker getIPSeeker(HttpServletRequest request) {
		
		return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String ip = "";
		Boolean flag = false;
		
		String forwaredFor = req.getHeader("X-Forwarded-For");
		String remoteAddr = req.getRemoteAddr();
		
		if (StringUtils.isNotEmpty(forwaredFor)) {
			
			ip = forwaredFor.split(",")[0];
		} else {
			
			ip = remoteAddr;
		}
		//ip = "116.179.32.35";
		ip = "112.199.92.42";
		
		String source_go_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + req.getContextPath() + req.getServletPath();
		
		log.info("Request ip:" + ip);
		log.info("Request url:" + source_go_url);
		
		if (!isIP(ip)) {
			
			res.sendRedirect("/forbiddenPage.html");
			return;
		}
		
		String[] ips = ip.replace('.', ',').split(",");
		String forTwo = ips[0] + "." + ips[1] + "." + "***" + "." + "***";
		String forThree = ips[0] + "." + ips[1] + "." + ips[2] + "." + "***";
		
		// 通过地址
		String allow = "";		
		// 拒绝地址
		String deny = "";
		// 拒绝地区
		String area = "";
				
		try {
			
			allow = RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT01.getCode());
			deny = RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT02.getCode());
			area = RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT03.getCode());
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error("从缓存服务器获取缓存数据发生异常，异常信息：" + e.getMessage());
		}
		
		try {
		
			allow = convertValue(allow);
			deny = convertValue(deny);
			area = convertValue(area);
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error("数据转换异常，异常信息：" + e.getMessage());
		}
		
		log.info("======allow======" + allow);
		log.info("======deny======" + deny);
		log.info("======area======" + area);
		
		if (deny.indexOf(ip) >= 0 || deny.indexOf(forTwo) >= 0 || deny.indexOf(forThree) >= 0) {
			
			log.info("deny ip:" + ip);
			res.sendRedirect("/forbiddenPage.html");
		} else if (allow.indexOf(ip) >= 0 || allow.indexOf(forTwo) >= 0 || allow.indexOf(forThree) >= 0) {
			
			log.warn("allow ip:" + ip);
			chain.doFilter(request, response);
		} else {
			
			IPSeeker ipSeeker = getIPSeeker(req);
			
			if (ipSeeker != null) {

				String areaName = ipSeeker.getCountry(ip);
				log.info("areaName:" + areaName);
				
				if (StringUtils.isNotBlank(areaName) && StringUtils.isNotBlank(area)) {
					
					for (String str : area.split(",")) {
						
						if (areaName.indexOf(str) != -1) {
							
							flag = true;
							break;
						}
					}
				}
				
				if (flag) {
					
					log.info("Deny Request ip:" + ip + "屏蔽地区:" + areaName);
					res.sendRedirect("/forbiddenPage.html");
				} else {

					chain.doFilter(request, response);
				}
			} else {
				
				log.warn("cant find instance of ipseeker");
				chain.doFilter(request, response);
			}
		}
	}

	private static boolean isIP(String ip) {
		
		if (StringUtils.isBlank(ip) || ip.length() < 7 || ip.length() > 15) {
			
			return false;
		}
		
		String reg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				   + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		
        Pattern pattern = Pattern.compile(reg);  
        Matcher matcher = pattern.matcher(ip);
        
        return matcher.matches();
	}
	
	private String convertValue(String str) {
		
		String value = "";
		
		if (StringUtils.isNotBlank(str)) {
		
			JSONObject jsonobject = JSONObject.fromObject(str);
			value = String.valueOf(jsonobject.get("value"));
		}
		
		return value;
	}
	
	public void init(FilterConfig config) throws ServletException {
	
	}
}