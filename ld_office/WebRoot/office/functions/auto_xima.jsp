<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>自动结算洗码</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript">
function stopAutoXima(){
	var frm=document.getElementById("mainform");
	var remark=window.prompt("您是否要中止后台自动任务，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		frm.remark.value=remark;
	}	
}

function ReadExcel() 
{ 
var tempStr = ""; 
var filePath= document.all.upfile.value; 
try{ 
var oXL = new ActiveXObject("Excel.application"); 
var oWB = oXL.Workbooks.open(filePath); 
oWB.worksheets(1).select(); 
var oSheet = oWB.ActiveSheet;
var i = 1;
while (true)
{
col1=oSheet.cells(i,1).value;
col2=oSheet.cells(i,2).value;
if (typeof(col1) == "undefined")
{
   break;
}

//组合XML
//document.write(i + "<br>"+col1+col2);
alert(col1+" "+col2);

i++;
}

}catch(e) 
{ 
	alert(e.name + ": " + e.message);
}
oXL.Quit(); 
oXL = null;
idTmr = window.setInterval("Cleanup();",1);
}

function Cleanup() {
    window.clearInterval(idTmr);
    CollectGarbage();
}
</script>
</head>
<body>
<!-- if(!window.confirm('确定?')) return; -->
<div id="excel_menu_left">
结算 --> 自动结算洗码 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<s:if test="#request.task==null || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@INIT.code || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@STOPPED.code || #request.task.flag==@dfh.model.enums.AutoTaskFlagType@EXCUTED.code">
<p align="left">&nbsp;&nbsp;上一次执行结果(编号<s:property value="#request.task.id"/>:<s:property value="@dfh.model.enums.AutoTaskFlagType@getText(#request.task.flag)"/>)</p>
<s:form action="autoGenerateXima"  namespace="/office" onsubmit="document.getElementById('sub_bottom').disabled=true;" name="mainform" id="mainform" theme="simple">
<s:date name="@dfh.utils.DateUtil@getOneHourAgo()" format="yyyy-MM-dd HH:mm:ss" var="endTime" />
<p align="left">开始时间<s:textfield name="startTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{@dfh.utils.DateUtil@getMontHreduce(@dfh.utils.DateUtil@getToday(),-6)}" />&nbsp;&nbsp;结束时间:<s:textfield name="endTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
<s:submit id="sub_bottom" value="结算至" /></p>
</s:form>
</s:if>
<s:elseif test="#request.task.flag==@dfh.model.enums.AutoTaskFlagType@PROCEED.code">
<p align="left">&nbsp;&nbsp;后台正在自动结算洗码中...</p>
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
<s:form action="stopAutoXima" onsubmit="stopAutoXima();" namespace="/office" name="mainform" id="mainform" theme="simple">
<s:hidden name="taskID" value="%{#request.task.id}" />
<s:hidden name="remark" />
<p align="left">&nbsp;&nbsp;<s:submit value="中止" align="left"/></p>
</s:form>
</s:elseif>
<s:elseif test="#request.task.flag==@dfh.model.enums.AutoTaskFlagType@GENERATED.code">
<c:redirect url="/office/ximalist.do"></c:redirect>
</s:elseif>
<s:else>
<p align="left" >正在执行洗码中，请稍等...</p>
</s:else>
</div>

<br/>

<c:import url="/office/script.jsp" />
</body>
</html>

