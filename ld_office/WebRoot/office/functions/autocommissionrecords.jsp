<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>自动结算佣金</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript">
function stopAutoCmn(){
	var frm=document.getElementById("mainform");
	var remark=window.prompt("您是否要中止后台自动任务，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		frm.remark.value=remark;
	}	
}
</script>
</head>
<body>
<!-- if(!window.confirm('确定?')) return; -->
<div id="excel_menu_left">
结算 --> 自动结算佣金 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<s:if test="#request.task==null || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@INIT.code || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@STOPPED.code || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@EXCUTED.code">
<p align="left">&nbsp;&nbsp;上一次执行结果(编号<s:property value="#request.task.id"/>):<s:property value="@dfh.model.enums.AutoTaskFlagType@getText(#request.task.flag)"/></p>
<s:form action="autoGenerateCommissionrecords"  namespace="/office" name="mainform" id="mainform" theme="simple">
<s:date name="@dfh.utils.DateUtil@getOneHourAgo()" format="yyyy-MM-dd HH:mm:ss" var="endTime" />
<p align="left">&nbsp;&nbsp;结算:
年份:<s:textfield name="year" onfocus="WdatePicker({dateFmt:'yyyy'})" size="10" My97Mark="false"></s:textfield>
月份<s:textfield name="month" onfocus="WdatePicker({dateFmt:'MM'})" size="10" My97Mark="false"></s:textfield>
<s:submit value="结算至" /></p>
</s:form>
</s:if>

<s:elseif test="#request.task.flag==@dfh.model.enums.AutoTaskFlagType@PROCEED.code">
<p align="left">&nbsp;&nbsp;后台正在自动结算佣金中...</p>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">编号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">开始时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">总数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">处理成功</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">处理失败</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">刷新时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">操作员</td>
            </tr>
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:property value="#request.task.id"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.task.startTime"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:property value="#request.task.totalRecords"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:property value="#request.task.finishRecords"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:property value="#request.task.failRecords"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.task.refreshTime"/></td>
          	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/><s:property value="#request.task.operator"/></td>
            </tr>
</table>
<s:form action="stopAutoCmn" onsubmit="stopAutoCmn();" namespace="/office" name="mainform" id="mainform" theme="simple">
<s:hidden name="taskID" value="%{#request.task.id}" />
<s:hidden name="remark" />
<p align="left">&nbsp;&nbsp;<s:submit value="中止" align="left"/></p>
</s:form>
</s:elseif>



<s:elseif test="#request.task.flag==@dfh.model.enums.AutoTaskFlagType@GENERATED.code">
<s:form action="autoExcuteCommissionrecords"  namespace="/office" name="mainform" id="mainform" theme="simple">
<p align="left">&nbsp;&nbsp;佣金已生成完毕，请确认后执行(执行需要较长的时间，请耐心在该页面内等待)
<s:submit value="确认执行" /></p>
</s:form>
</s:elseif>
<s:else>
<p align="left" >正在执行佣金中，请稍等...</p>
</s:else>
</div>

<br/>

<c:import url="/office/script.jsp" />
</body>
</html>

