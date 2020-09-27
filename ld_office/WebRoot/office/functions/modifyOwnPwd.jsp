<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改密码</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="excel_menu_left">
其他  --> 修改密码 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<s:form action="modifyOwnPwd" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<p align="left">用户名:&nbsp;&nbsp;&nbsp;&nbsp;<s:textfield name="loginname" size="30" disabled="true" value="%{#session.operator.username}"/></p>
<p align="left">旧密码:&nbsp;&nbsp;&nbsp;&nbsp;<s:password name="oldPassword" size="30" /></p>
<p align="left">新密码:&nbsp;&nbsp;&nbsp;&nbsp;<s:password name="newPassword" size="30" /></p>
<p align="left">重复新密码:<s:password name="retypePassword" size="30" /></p>
<tr>
  <td height="33"><span style="padding-left:2px"><s:text name="验证码"/></span></td>
   <s:url action="validateCodeForIndex" namespace="/jsp" var="imgCode"></s:url>
  <td width="90" height="33"><s:textfield name="validateCode"  size="8" cssStyle="border:1px solid #2a0e02"  maxlength="10" onfocus="document.getElementById('imgCode').style.display='block';document.getElementById('imgCode').src='%{imgCode}?r='+Math.random();"/></td>
  <td width="79"><img id="imgCode" src=""  title="<s:text name="validateCode.tip"/>" onclick="document.getElementById('imgCode').src='<c:url value='/jsp/validateCodeForIndex.do' />?r='+Math.random();" style="cursor: pointer;display:none;" /></td>
</tr>
<p align="left"><s:submit value="修改密码" align="left" /></p>
</s:form>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

