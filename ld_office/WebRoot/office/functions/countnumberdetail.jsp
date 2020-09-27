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
    <base href="<%=basePath%>">
    
    <title>My JSP 'countnumberdetail.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
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
  
  <body style="background:#b6d9e4;font-size:11px">
  	<br/>
    <span>电话回访-->统计回访结果-->回访结果明细</span>&nbsp;&nbsp;
	<a href="/office/functions/countnumber.jsp"><font color="red">上一步</font></a>
    <div align="center" style="width:100%">
    
    	<s:if test="pageList!=null">
    		<display:table name="pageList" id="fc" requestURI="/telvisit/getTelTaskCount.do" style="width:1000px" decorator="dfh.displaytag.util.TelvisitThreeFormat">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="tasknamein" title="任务名称" style="text-align:left" ></display:column>
			    	<display:column property="createtime" title="任务添加时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
			    	<display:column property="operator" title="操作员工" style="text-align:center" ></display:column>
			    	<display:column property="sum" title="总拨打数" style="text-align:center" ></display:column>
			    	<display:column property="success" title="成功数" style="text-align:center"  ></display:column>
			    	<display:column property="fail" title="失败数" style="text-align:center"></display:column>
			    	<display:column property="taskstatus" title="任务状态" style="text-align:center"></display:column>
			    	
			    </display:table>
			    
		</s:if>
    </div>
  </body>
</html>
