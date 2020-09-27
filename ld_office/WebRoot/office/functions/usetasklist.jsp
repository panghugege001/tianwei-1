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
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
     <br>
     <p>电话回访-->使用任务列表</p>
    <br/>
     <div align="center" style="width:100%">
	    <s:form action="getTaskList" namespace="/telvisit" method="post"  theme="simple">
		   	<table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
		   	
		    	<tr align="left">
		    		<td>
		    			任务名称：<s:textfield name="taskname" size="30"></s:textfield>
		    		</td>
		    		<td>
		    			任务状态：<s:select name="taskstatus" list="#{'-1':'全部','0':'未完成','1':'完成','2':'废弃'}" emptyOption="false" listKey="key" listValue="value"></s:select>
		    		</td>
		    		<td>
		    			起始时间:<s:textfield  size="25" name="start" value="%{startTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate" ></s:textfield>
		    		</td>
		    		<td>
		    			结束时间:<s:textfield  size="25" name="end" value="%{endTime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  cssClass="Wdate"></s:textfield>
		    		</td>
		    		<td>
		    			<s:submit value="查  询"></s:submit>
		    		</td>
		    	</tr>
		   	
		    </table>
	    </s:form>
    </div>
    <div align="center" style="width:100%">
    	<s:if test="pageList!=null">
    		<display:table name="pageList" id="fc" requestURI="/telvisit/getTaskList.do" style="width:1000px" decorator="dfh.displaytag.util.TelvisittaskFormat">
					<display:column title="序号" style="text-align:center">${fc_rowNum}</display:column>
			    	<display:column property="taskname" title="任务名称" style="text-align:left"></display:column>
			    	<display:column property="addtime" title="发布时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
			    	<display:column property="starttime" title="任务起始时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
			    	<display:column property="endtime" title="任务结束时间" style="text-align:center" format="{0,date,yyyy-MM-dd HH:mm:ss}"></display:column>
			    	<display:column property="taskstatus" title="任务状态" style="text-align:center"></display:column>
			    	<display:column property="cz" title="操  作" style="text-align:center"></display:column>
			    </display:table>
		</s:if>
    </div>
  </body>
</html>
