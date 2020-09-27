<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>银行资料</title>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
</head>
<body>
<div id="excel_menu_left">
账户 --> 银行资料 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left"><s:fielderror/></p>
<s:if test="#request.bankinfo!=null">
<table align="left">
<tr><td>帐号:</td><td><s:property value="#request.bankinfo.loginname"/></td></tr>
<tr><td>真实姓名:</td><td><s:property value="#request.bankinfo.accountName"/></td></tr>
<tr><td>银行帐号:</td><td><s:property value="#request.bankinfo.accountNo"/></td></tr>
<tr><td>银行类别:</td><td><s:property value="#request.bankinfo.bank"/></td></tr>
<tr><td>账户类别:</td><td><s:property value="#request.bankinfo.accountType"/></td></tr>
<tr><td>银行网点:</td><td><s:property value="#request.bankinfo.bankAddress"/></td></tr>
<tr><td>开户城市:</td><td><s:property value="#request.bankinfo.accountCity"/></td></tr>
</table>
</s:if>
<s:else>
该会员未创建银行资料
</s:else>
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

