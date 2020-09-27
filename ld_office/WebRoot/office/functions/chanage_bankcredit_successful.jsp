<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>手工增减银行额度</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 手工增减银行额度<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<p align="left" >&nbsp;&nbsp;为银行账户<s:property value="#parameters.loginname"/><s:if test="@java.lang.Integer@parseInt(#parameters.isAdd)==0">增加</s:if><s:else>扣除</s:else>额度<s:property value="#parameters.amount"/>,操作成功</p>
</div>
<br/>

<c:import url="/office/script.jsp" />
</body>
</html>

