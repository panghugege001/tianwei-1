<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>银改提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getAccountInfoForRebankinfo.do' />";
frm.submit();
}
</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 银改提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left"><s:fielderror/></p>
<s:form action="addRebankInfoProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:</td><td><s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text"/></td></tr>
<tr><td>会员帐号:</td><td><s:textfield name="loginname" size="30" onblur="loadInfo();"/></td></tr>
<tr><td>银行帐号:</td><td><s:textfield name="accountNo" size="30"/></td></tr>
<tr><td>帐号姓名:</td><td><s:textfield name="accountName" size="30" readonly="true"/></td></tr>
<tr><td>帐号类型:</td><td><s:textfield name="accountType" size="30"/></td></tr>
<tr><td>开户城市:</td><td><s:textfield name="accountCity" size="30"/></td></tr>
<tr><td>银行类别:</td><td><s:textfield name="bank" size="30"/></td></tr>
<tr><td>银行网点:</td><td><s:textfield name="bankAddress" size="30"/></td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
<tr><td align="center"><s:submit value="提交" /><s:reset value="重置"/></td><td></td></tr>
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

