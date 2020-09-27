<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.action.vo.AnnouncementVO" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>

<link rel="stylesheet" type="text/css" href="/css/news.css" />
<script type="text/javascript" src="/js/news.js"></script>
<cache:cache key="headnewsCache" scope="application" time="300">

<%
List<AnnouncementVO> headnewsList = AxisSecurityEncryptUtil.queryTopNews();
if(headnewsList==null){
	headnewsList=new ArrayList();
}
application.setAttribute("headnewsList",headnewsList);
%>
<s:url action="queryAnnObject" namespace="/asp" var="queryAnnObjectUrl"></s:url>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td height="2px"></td>
	</tr>
	<tr>
		<td>
			
				<s:if test="#application.headnewsList.size()<=0">
					暂无公告
				</s:if>
				<s:else>
					<div class="news-main left">
					    <div class="news-txt clear">
					        <div class="news-list">
					            <ul>
					            	<s:iterator value="#application.headnewsList" var="indexTopNews">
										<li><a style="text-decoration: none;" href="${queryAnnObjectUrl }?aid=${id }">${title }</a></li>
									</s:iterator>
					            	
					            </ul>
					        </div>
					    </div>
					</div>
					
				</s:else>
						
				
		</td>
	</tr>
</table>

</cache:cache>
