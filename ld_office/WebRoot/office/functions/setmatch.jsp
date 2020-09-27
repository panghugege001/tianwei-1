<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.utils.Constants"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设置上级用户</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="excel_menu_left">
操作 --> 设置擂台赛用户 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addMatch" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td><span style='color:red'>*</span>会员账号:</td><td><s:textfield name="loginname" size="30"/><span style="color:red">如果设置成功系统将自己动送出<%=Constants.MATCH_ADDMONEY %>元</span></td></tr>
<tr><td align="center"><s:submit value="确定提交" /></td><td></td></tr>
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

