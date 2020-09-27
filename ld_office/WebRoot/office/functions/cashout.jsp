<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>取款提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getAccountInfoForCashout.do' />";
frm.submit();
}
</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 取款提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left"><s:fielderror/><s:actionerror/></p>
<s:form action="addCashOutProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:</td><td><s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text"/></td></tr>
<tr><td>提款帐号:</td><td><s:textfield name="loginname" size="30" onblur="loadInfo();"/></td></tr>
<tr><td>用户的当前总额度:</td><td><s:textfield name="credit" size="30" readonly="true" value="%{#request.credit}"/></td></tr>
<tr><td>提款金额:</td><td><s:textfield name="amount" size="30" />单笔提款金额必须小于19万</td></tr>
<tr><td>银行帐号:</td><td><s:textfield name="accountNo" size="30"/></td></tr>
<tr><td>帐号姓名:</td><td><s:textfield name="accountName" readonly="true" size="30" /></td></tr>
<tr><td>帐号类型:</td><td><s:select name="accountType" emptyOption="true" list="{'借记卡','信用卡'}" /></td></tr>
<tr><td>开户城市:</td><td><s:textfield name="accountCity" size="30"/></td></tr>
<tr><td>银行类别:</td><td><s:select name="bank" list="%{#application.IssuingBankEnum}" listKey="issuingBank" listValue="issuingBankCode" /></td></tr>
<tr><td>银行网点:</td><td><s:textfield name="bankAddress" size="30"/></td></tr>
<tr><td>电话:</td><td><s:textfield name="phone" size="30"/></td></tr>
<tr><td>邮箱:</td><td><s:textfield name="email" size="30"/></td></tr>
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

