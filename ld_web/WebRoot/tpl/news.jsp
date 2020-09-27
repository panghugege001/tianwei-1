<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.action.vo.AnnouncementVO"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	response.setHeader("pragma", "no-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);

	List<AnnouncementVO> list = AxisSecurityEncryptUtil.query();
	if (list == null) {
		list = new ArrayList();
	}
	request.setAttribute("list", list);
%>

 
<s:url action="queryAnnObject" namespace="/asp" var="queryAnnObjectUrl"></s:url>
<s:if test="#request.list.size()<=0">
	<li class="item"><a class="marquee-item">暂无公告...</a><li>
</s:if>
<s:else>
	<s:iterator value="#request.list" var="indexTopNews" status="index">
		<li class="item"><a  class="marquee-item" target="_blank"   href="/newsList.jsp">
			<s:property value="#index.index+1"/>、${title }&nbsp;&nbsp;&nbsp;</a></li>
	</s:iterator>
</s:else>
 

