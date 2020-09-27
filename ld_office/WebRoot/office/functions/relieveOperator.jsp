<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>解除禁用</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="excel_menu_left">
其他  --> 解除禁用<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<s:form action="relieveOperator" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<p align="left">用户名:&nbsp;&nbsp;&nbsp;&nbsp;<input name="loginname" size="30"/></p>
<p align="left"><s:submit value="解除禁用" align="left" /></p>
</s:form>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

