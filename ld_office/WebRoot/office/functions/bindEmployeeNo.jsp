<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>绑定员工编号</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="excel_menu_left">
其他  --> 绑定员工编号<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<form action=""></form>
<s:form action="bindEmployeeNo" onsubmit="return reminder();" namespace="/office" name="mainform" id="mainform" theme="simple">
<p align="left">员工编号:&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="employeeNo" /></p>
<p align="left"><s:submit value="绑定员工编号" align="left"/></p>
</s:form>
</div>
<c:import url="/office/script.jsp" />
<script type="text/javascript">
	function reminder(){
		if(confirm("温馨提示：\r\n只能绑定一次，如果绑定错误请联系产品经理修改。\r\n你确认要执行此操作么？")){
			return true;
		}else{
			return false;
		}
	}
</script>
</body>
</html>