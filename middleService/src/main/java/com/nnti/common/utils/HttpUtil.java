package com.nnti.common.utils;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import com.nnti.common.constants.Constant;
import com.nnti.common.extend.SSLSecureProtocolSocketFactory;
import net.sf.json.JSONObject;

public class HttpUtil {

	@SuppressWarnings("deprecation")
	public static Protocol https = new Protocol("https", new SSLSecureProtocolSocketFactory(), 443);

	private static Integer TIME_OUT = 500000;

	public static HttpClient createHttpClient() {

		HttpClient httpclient = new HttpClient();

		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", Constant.ENCODE);
		params.setParameter("http.socket.timeout", TIME_OUT);

		httpclient.setParams(params);

		return httpclient;
	}

	public static HttpClient createHttpClientShort() {

		HttpClient httpclient = new HttpClient();

		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", Constant.ENCODE);
		params.setParameter("http.socket.timeout", 5000);

		httpclient.setParams(params);

		return httpclient;
	}

	@SuppressWarnings("rawtypes")
	public static String getURLJson(String action, JSONObject params) {

		String url = "";

		if (action == null) {

			return url;
		}

		if ((params != null) && (params.entrySet().size() > 0)) {

			url = action + "?";

			Iterator it = params.keySet().iterator();

			while (it.hasNext()) {

				String key = (String) it.next();
				String value = (String) params.get(key);

				if (url.endsWith("?")) {

					url = url + key.toLowerCase() + "=" + value;
				} else {

					url = url + "&" + key.toLowerCase() + "=" + value;
				}
			}
		} else {

			url = action;
		}

		return url;
	}

	public static String getIp(HttpServletRequest request) {

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
		} else {

			return ip;
		}
	}
}