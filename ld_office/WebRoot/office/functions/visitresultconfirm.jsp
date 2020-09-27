<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <style type="text/css">
    	table{
    		font-size:12px;
    		}
    </style>
    <script type="text/javascript">
    	function sub(){
    		if(visitResultForm.visitresult.value.Trim().length==0){
					alert("[回访明细]不可以为空");
            		return false;
    		}
    		return true;
    	}

    	 String.prototype.Trim=function()
    	          {
    	                  return this.replace(/(^\s*)|(\s*$)/g,"");
    	          }
    </script>
  </head>
  
  <body style="background:#b6d9e4;font-size:12px;">
  <s:url action="nextpage" namespace="/telvisit" var="nextURL">
  		<s:param name="visitid" value="visitid"></s:param>
  		<s:param name="page" value="page"></s:param>
  		<s:param name="taskid" value="taskid"></s:param>
  		<s:param name="taskName" value="taskName"></s:param>
  </s:url>
   <br>
     <p>电话回访-->使用任务列表-->任务列表明细-->回访结果录入&nbsp;&nbsp;<a href="${nextURL}"> <font color="red">上一步</font></a></p>
    <br/>
	<s:form action="visitResult" namespace="/telvisit" method="post" theme="simple" id="visitResultForm" name="visitResultForm" onsubmit="return sub()">
		<s:hidden name="taskName"></s:hidden>
		<s:hidden name="intro"></s:hidden>
		<s:hidden name="islock"></s:hidden>
		<s:hidden name="visitid"></s:hidden>
		<s:hidden name="page"></s:hidden>
		<s:hidden name="taskid"></s:hidden>
		
		<table border="1" align="center" width="600px" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="2"><font color="red">注意：回访结果列表中，选择[成功]或[失败]并提交后，系统会认为该客户在该次任务中访问完成，将不可再次回访！</font></td>
			</tr>
			<tr>
				<td>任务名称:</td><td width="520px"><FONT color="blue" size="3px"><s:property value="taskName" /></FONT></td>
			</tr>
			<tr>
				<td>回访对象:</td><td><FONT color="blue" size="4px"><s:property value="loginname"/></FONT></td>
			</tr>
			<tr>
				<td>回访结果:</td><td><s:select name="execstatus"  theme="simple" list="#{'0':'失败','1':'成功','2':'未访问','3':'已访问'}" emptyOption="false" listKey="key" listValue="value"></s:select></td>
			</tr>
			<tr>
				<td colspan="2">回访明细:</td>
			</tr>
			<tr>
				<td colspan="2"><s:textarea name="visitresult" theme="simple" rows="5" cols="80"></s:textarea></td>
			</tr>
			<tr><td colspan="2" align="center"><s:submit theme="simple" value="提  交"/></td></tr>
		</table>
		<table border="1" cellpadding="0" cellspacing="0" width="100%" align="left">
			<tr><td colspan="5">回访历史:</td></tr>
			<tr>
				<th width="50px" align="center">序号</th>
				<th width="150px" align="center">操作员</th>
				<th width="200px" align="center">访问时间</th>
				<th width="150px" align="center">访问结果</th>
				<th align="center">访问明细</th>
			</tr>
			
			<s:iterator value="#request.remarks" status="st" >
				<tr>
					<td align="center">${st.count }</td>
					<td align="left"><s:property value="operator"/></td>
					<td align="center"><s:property value="@dfh.utils.DateUtil@formatDateForStandard(#request.addtime)"/></td>
					<td align="center"><s:property value="@dfh.utils.StringUtil@execStatuc2String(#request.execstatus)"/></td>
					<td align="left"><s:property value="remark"/></td>
				</tr>
			</s:iterator>
		</table>
	</s:form>
	
  </body>
</html>
