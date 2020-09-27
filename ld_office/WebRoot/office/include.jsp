<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/nnti.tld" prefix="nnti" %>
<%
	ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
	String image_path="/"+request.getContextPath()+"/images/";
%>
<s:url value="/images" var="image_path"></s:url>
<s:set var="start" value="@dfh.utils.DateUtil@getToday()" />
<s:set var="startPt" value="@dfh.utils.DateUtil@ptStart()" />
<s:set var="endPt" value="@dfh.utils.DateUtil@ptEnd()" />
<s:set var="startNT" value="@dfh.utils.DateUtil@ntStart()" />
<s:set var="endNT" value="@dfh.utils.DateUtil@ntEnd()" />
<s:set var="startYyyyMM" value="@dfh.utils.DateUtil@startYyyyMM()" />
<s:set var="endYyyyMM" value="@dfh.utils.DateUtil@endYyyyMM()" />
<s:set var="end" value="@dfh.utils.DateUtil@getTomorrow()" />
<s:set var="year" value="@dfh.utils.DateUtil@getYear(@dfh.utils.DateUtil@getMontToDate(@dfh.utils.DateUtil@getToday(),-30))" />
<s:set var="month" value="@dfh.utils.DateUtil@getMonth(@dfh.utils.DateUtil@getMontToDate(@dfh.utils.DateUtil@getToday(),-30))" />
<s:set var="vyear" value="%{year}"/>
<s:set var="vmonth" value="%{month}"/>
<s:set var="yesterday" value="@dfh.utils.DateUtil@getchangedDateStr(-1,@dfh.utils.DateUtil@getYYYY_MM_DD())" />
<s:set var="today" value="@dfh.utils.DateUtil@getchangedDateStr(0,@dfh.utils.DateUtil@getYYYY_MM_DD())" />
<s:set var="stringStartTime" value="@dfh.utils.DateUtil@getTodayFormat()" />
<s:set var="stringEndTime" value="@dfh.utils.DateUtil@getTomorrowFormat()" />
<s:date name="start" format="yyyy-MM-dd HH:mm:ss" var="startTime"/>
<s:date name="end" format="yyyy-MM-dd HH:mm:ss" var="endTime"/>
<s:date name="start" format="yyyy-MM-dd HH:mm:ss" var="operateStartTime"/>
<s:date name="end" format="yyyy-MM-dd HH:mm:ss" var="operateEndTime"/>
<s:date name="startPt" format="yyyy-MM-dd HH:mm:ss" var="startPt"/>
<s:date name="endPt" format="yyyy-MM-dd HH:mm:ss" var="endPt"/>
<s:date name="yesterday" format="yyyy-MM-dd" var="yesterday"/>
<s:date name="today" format="yyyy-MM-dd" var="today"/>
<s:date name="startcreateTime" format="yyyy-MM-dd HH:mm:ss" var="startcreateTime"/>
<s:date name="endcreateTime" format="yyyy-MM-dd HH:mm:ss" var="endcreateTime"/>
<%-- <s:date name="startNT" format="yyyy-MM-dd HH:mm:ss" var="startNt"/>
<s:date name="endNT" format="yyyy-MM-dd HH:mm:ss" var="endNt"/> --%>

<s:if test="#session.operator==null">
	<script type="text/javascript">
		alert("会话已失效，请重新登陆!");top.location.href="<s:url value='/office/index.jsp' />";
	</script>
</s:if>
<%    
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>