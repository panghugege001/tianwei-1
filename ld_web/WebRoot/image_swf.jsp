<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.model.HdImage"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	response.setHeader("pragma", "no-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);
	HdImage imageSwf = AxisSecurityEncryptUtil.queryImageFlash();
	request.setAttribute("imageSwf", imageSwf);
%>
<c:if test="${imageSwf.image004!=null && imageSwf.image004!=''}">
	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0"
		width="810" height="300">
		<param name="wmode" value="transparent" />
		<param name="movie" value="${imageSwf.image004}" />
		<param name="quality" value="high" />
		<embed src="${imageSwf.image004}" width="810" height="300"
			quality="high"
			pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash"
			type="application/x-shockwave-flash" wmode="transparent"></embed>
	</object>
</c:if>


