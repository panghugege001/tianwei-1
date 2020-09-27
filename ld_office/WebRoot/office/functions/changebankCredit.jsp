<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<title>手工增减银行额度</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getAccountInfoForChangebankCredit.do' />";
frm.submit();
}
</script>
</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 手工增减银行额度
			<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="changebankCreditManual" onsubmit="submitonce(this);" namespace="/office" id="mainform" theme="simple">
			<table align="left" border="0">
				<tr><td>银行帐号:</td><td><s:textfield name="loginname" size="30" onblur="loadInfo();"/></td></tr>
				<tr><td>银行当前总额度:</td><td><s:textfield name="credit" size="30" disabled="true" value="%{#request.credit}"/></td></tr>
				<tr><td>增加/扣除:</td><td><s:select name="isAdd" list="#{1:'扣除',0:'增加'}" /></td></tr>
				<tr><td>类型:</td><td><s:select name="type" list="#{'CHANGE_CROSSPLATFORM':'跨平台转账','CHANGE_QUOTAPROBLEM':'后台额度问题','CHANGE_ARBITRAGE':'套汇'}" /></td></tr>
				<tr><td>额度:</td><td><s:textfield name="amount" size="30"  /></td></tr>
				<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
				<tr><td align="center"><s:submit value="提交" /><s:reset value="重置"/></td><td></td></tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
