package com.nnti.office.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class IPUtil {
	
	public static String getIp(HttpServletRequest request) {
		String forwaredFor = request.getHeader("X-Forwarded-For");
		String remoteAddr = request.getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else {
			return remoteAddr;
		}
	}
	
}
