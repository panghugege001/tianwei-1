<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设定等级</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
</head>
<body>
<div id="excel_menu_left">
账户 --> 设定等级 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:form action="setLevel" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple" >
<p align="left"><s:fielderror/></p>
<table align="left">
<tr><td>会员帐号:</td><td><s:textfield name="loginname" value="%{#parameters.loginname}" readonly="true"/></td></tr>
<tr><td>等级:</td><td><s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" value="@java.lang.Integer@parseInt(#parameters.level)"/></td></tr>
<tr><td colspan="2"><s:submit value="修改"/></td></tr>
</table>
</s:form>
</div>

<br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

