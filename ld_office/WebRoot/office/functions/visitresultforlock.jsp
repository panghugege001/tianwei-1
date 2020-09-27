<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >

span.pagebanner {
	width: 1000px;
}
span.pagelinks {
	width: 1000px;
	
}

</style>
  </head>
  
    <body style="background:#b6d9e4;font-size:12px;">
    <s:url action="nextpageForCount" namespace="/telvisit" var="nextURL">
  		<s:param name="visitid" value="visitid"></s:param>
  		<s:param name="taskid" value="taskid"></s:param>
  		<s:param name="taskName" value="taskName"></s:param>
  </s:url>
   <br>
     <p>电话回访-->管理任务列表-->解锁任务明细-->回访明细&nbsp;&nbsp;<a href="${nextURL}"> <font color="red">上一步</font></a></p>
    <br/>
     <div align="center" style="width:100%">
     	<p>回访对象：<font color="blue"><s:property value="loginname"/></font></p>
    	<display:table name="pageList" id="fc" requestURI="/telvisit/getRemarksForLock.do" style="width:1000px">
				<display:column title="序号" style="text-align:center;width:50px">${fc_rowNum}</display:column>
		    	<display:column property="operator" title="操作员" style="text-align:left;width:100px"></display:column>
		    	<display:column property="addtime" title="回访时间" style="text-align:center;width:150px"  format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
		    	<display:column property="remark" title="回访明细" style="text-align:left"></display:column>
		    </display:table>
    </div>
  </body>
</html>
