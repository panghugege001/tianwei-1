<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>自动结算洗码</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body>
<div id="excel_menu_left">
结算 --> 自动结算洗码 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:url value="/office/showXima.do" var="showUrl"/>
<p align="left" >系统已经成功在后台自动结算洗码,查看进度，请点击<s:a href="%{showUrl}">这里</s:a> </p>
</div>
<br/>

<c:import url="/office/script.jsp" />
</body>
</html>

