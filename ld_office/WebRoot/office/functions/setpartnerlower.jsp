<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
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
操作 --> 设置合作伙伴下级用户 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="partnerSetlower" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >

<tr><td><span style='color:red'>*</span>合作伙伴账号:</td><td><s:textfield name="partner" id="loginname" size="30"/></td></tr>
<tr><td><span style='color:red'>*</span>确定合作伙伴:</td><td><s:textfield name="retypePartner" id="loginname" size="30"/></td></tr>

<tr><td><span style='color:red'>*</span>下级会员账号:</td><td><s:textfield name="loginname" size="30"/></td></tr>
<tr><td><span style='color:red'>*</span>确定下级账号:</td><td><s:textfield name="retypeLoginname" size="30"/></td></tr>
<tr><td align="center"><s:submit value="提交" /></td><td></td></tr>
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

