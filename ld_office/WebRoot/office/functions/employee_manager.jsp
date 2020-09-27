<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>内部员工管理首页</title>
    
	<%
	response.setHeader("pragma", "no-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);
	%>
	
	
<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >
div.search{
	position:absolute;
	top:15px;
	left:50px;
}

div.show{
	position:absolute;
	top:25px;
	left:50px;
}
span.pagebanner {
	width: 850px;
}
span.pagelinks {
	width: 850px;
	
}
</style>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
    	<p>其他--&gt;员工管理</p>
    	<div class="search">
    		<s:form action="byid" theme="simple" method="post" namespace="/employee">
    			员工账号:<s:textfield name="eName"></s:textfield><s:submit value="检  索">&nbsp;&nbsp;&nbsp;&nbsp;<font color="blue">员工账号为空，即查询本部门的全部员工</font>
    			</s:submit></s:form>
    	</div>
    	<div class="show" >
    		<s:if test="pageList!=null">
    			<display:table name="pageList" requestURI="/employee/byid.do" id="emp" decorator="dfh.displaytag.util.EmployeeFormat" style="width:850px">
	    			<display:column title="序号" style="text-align:center">${emp_rowNum}</display:column>
	    			<display:column title="员工账号" property="username"></display:column>
	    			<display:column title="部门" property="authority" style="text-align:center"></display:column>
	    			<display:column title="登录次数" property="loginTimes"></display:column>
	    			<display:column title="创建时间" property="createTime" style="text-align:center"></display:column>
	    			<display:column title="最后登录时间" property="lastLoginTime" style="text-align:center"></display:column>
	    			<display:column title="最后登录IP" property="lastLoginIp"></display:column>
	    			<display:column title="状态" property="enabled" style="text-align:center"></display:column>
	    		    <display:column title="国信坐席号码" property="phonenoGX" style="text-align:center"></display:column>
	    		    <display:column title="比邻坐席号码" property="phonenoBL" style="text-align:center"></display:column>
	    			<display:column title="操作" style="text-align:center" value="启用/禁用" url="/employee/isEnable.do" paramId="eName" paramProperty="username"></display:column>
	    		</display:table>
    		</s:if> 
    		 
    	</div>
    	<c:import url="/office/script.jsp" />
  </body>
</html>
