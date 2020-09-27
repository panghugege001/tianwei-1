<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>存款提案</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getUserInfo.do' />";
frm.submit();
}

function loadidInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getUserInfoById.do' />";
frm.submit();
}

</script>
</head>
<body>
<div id="excel_menu_left">
操作 --> 存款提案 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addCashinProposal" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>用户类别:<span style="color:red">*</span></td><td>
<s:select name="title" list="%{#application.UserRoleExceptFree}" listKey="code" listValue="text" value="MONEY_CUSTOMER"/></td></tr>
<tr><td>会员帐号:<span style="color:red">*</span></td><td><s:textfield name="loginname" size="30" onblur="loadInfo()"  value="%{#request.user.loginname}" /></td></tr>
<tr><td>会员ID:</td><td><s:textfield name="id" size="30" onblur="loadidInfo()"/></td></tr>
<tr><td>存款姓名:<span style="color:red">*</span></td><td><s:textfield name="accountName" size="30" value="%{#request.user.accountName}"/></td></tr>
<tr><td>存款额度:<span style="color:red">*</span></td><td><s:textfield name="amount" size="30"/></td></tr>
<tr><td>手续费:<span style="color:red">*</span></td><td><s:textfield name="fee" size="30" value="0.0" /></td></tr>
<tr><td>存款账户:<span style="color:red">*</span></td>
<td><s:action name="getSaveBankAccount" namespace="/bankinfo" id="bean" />
<s:select  headerValue="请选择存款账户" headerKey="0"
             list="#bean.bankinfos" name="bankinfoid" listKey="id" listValue="username"></s:select>
       </td></tr>
 <tr><td>存款方式:<span style="color:red">*</span></td>
 <td>
 <s:select cssStyle="width:60px" list="#{'':'','网银':'网银','ATM':'ATM','现存':'现存','网转':'网转','汇款':'汇款'}" name="saveway" listKey="key" listValue="value" emptyOption="false"></s:select>
 </td></tr>
<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
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

