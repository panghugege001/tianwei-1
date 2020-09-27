<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
    <title>修改任务</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript">
		function submitForNewAction(btn,action,pno){ 
			btn.disabled=true;
			var taskname = document.getElementById("taskname").value;
			var start = document.getElementById("starttime").value;
			var end = document.getElementById("endtime").value;
			
				var xmlhttp = new Ajax.Request(    
					action,
		        	{    
		            	method: 'post',
		            	parameters:"id="+pno+"&r="+Math.random()+"&taskname="+taskname+"&start="+start+"&end="+end,
		            	onComplete: responseMethod  
		        	}
	    		);
			

		}
		
		function unlockTelVisit(btn,action,pno){
			btn.disabled=true;
			if(confirm("你确认要解除锁定么？")){
		 		var xmlhttp = new Ajax.Request(    
					action,
		        	{    
		            	method: 'post',
		            	parameters:"id="+pno+"&r="+Math.random(),
		            	onComplete: responseMethod  
		        	}
	    		);

			}else{
				btn.disabled=false;
			}
		}
		
		function responseMethod(data){
			
			alert(data.responseText);
			document.getElementById("save").disabled=false;
			window.location.reload(); 
		}
	</script>

  </head>
  
  <body style="background:#b6d9e4;font-size:11px">
   <br/>
    <p>电话回访-->管理任务列表-->修改任务内容</p>&nbsp;&nbsp;
    <br/><br/><br/><br/>
    <div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
    	<s:form action="updateTask" namespace="/telvisit" name="mainform" id="mainform" theme="simple">
    		<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
    			<tr>
    				<td>任务名称:<input name="taskname" id="taskname" value="<c:out value="${telvisittask.taskname}"></c:out>"/>
    				</td>
    				<td>任务开始时间:
    				<input name="starttime" size="18" maxlength="18" id="starttime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="<c:out value="${telvisittask.starttime}"></c:out>"/></td>
    				<td>预计结束时间:<input name="endtime" maxlength="18" size="18" id="endtime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="<c:out value="${telvisittask.endtime}"></c:out>"/></td>
    				<td>
    				<s:set var="jobPno" value="%{#request.telvisittask.id}" scope="request"/>
    				
    				<c:url var="action" value="/telvisit/updateTask.do"  scope="request" />
              		<input type="button" value="保存" id="save" onclick="submitForNewAction(this,'${action}','${jobPno}');"/>	</td>
              		<td><a href="/office/functions/managetask.jsp"><font color="red">返回</font></a></td>
    			</tr>
    		</table>
    	</s:form>
    </div>
    
  </body>
</html>
