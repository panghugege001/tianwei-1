<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>自动结算洗码</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="excel_menu_left">
结算 --> 自动结算洗码 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"></p>
<p align="left" ><span style="color:red;font-size:14px;">暂未找到符合洗码要求的用户</span></p>
</div>
<br/>
<c:import url="/office/script.jsp" />
</body>
</html>

