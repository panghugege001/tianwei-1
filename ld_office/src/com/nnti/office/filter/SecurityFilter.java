package com.nnti.office.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nnti.office.model.auth.Operator;
import com.nnti.office.service.auth.PermissionService;
import com.nnti.office.util.CollectionUtil;

import dfh.utils.Constants;

@Component
public class SecurityFilter implements Filter {
	
	@Autowired
	PermissionService permissionService;
	
	private static List<String> allProtectedUriList = new ArrayList<String>();
	
	private Logger logger = Logger.getLogger(SecurityFilter.class);
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if(allProtectedUriList.size() == 0) {
			iniProtectedUriList();
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String reqUri = httpServletRequest.getRequestURI();
		if(! allProtectedUriList.contains(reqUri)) {
			filterChain.doFilter(request, response);
			return;
		}
		List<String> userPermissionUrlList = (List<String>)httpServletRequest.getSession().getAttribute(Constants.SESSION_AUTH_PERMISSION);
		if(CollectionUtil.isNotEmpty(userPermissionUrlList) && userPermissionUrlList.contains(reqUri)) {
			filterChain.doFilter(request, response);
			return;
		}
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/office/access_denied.jsp");
	}
	
	public void iniProtectedUriList() {
		allProtectedUriList = permissionService.getAllProtectedUri();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}