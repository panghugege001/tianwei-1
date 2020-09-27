<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>生成试玩帐号</title>
<script defer type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
</head>
<body>
<div id="excel_menu_left">
账户 --> 生成试玩帐号 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:form action="generateFreeCustomer" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<p align="left"><s:submit value="生成试玩帐号" align="left" /></p>
<p align="left">
<s:property value="%{#request.output}"/>
</p>
</s:form>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

