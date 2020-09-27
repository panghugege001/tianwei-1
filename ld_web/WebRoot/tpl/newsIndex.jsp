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

<marquee id="notice-marquee" data-marquee="" class="marquee-list" behavior="scroll" direction="left" loop="infinite" scrollamount="3">
<s:url action="queryAnnObject" namespace="/asp" var="queryAnnObjectUrl"></s:url>
<s:if test="#request.list.size()<=0">
	<a class="marquee-item">暂无公告...</a>
</s:if>
<s:else>
	<s:iterator value="#request.list" var="indexTopNews" status="index">
		<a  class="marquee-item" target="_blank"   href="/newsList.jsp">
			<s:property value="#index.index+1"/>、${title }&nbsp;&nbsp;&nbsp;</a>
	</s:iterator>
</s:else>
</marquee>

