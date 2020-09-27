<%@page pageEncoding="UTF-8" %>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sd"  %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%
	ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
%>
<s:url value="/images" var="image_path" />
<s:url value="/images/hzhb" var="hzhb_office_image_path" />



 