<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<title>增减额度</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getAccountInfoForChangeQuotal.do' />";
frm.submit();
}
</script>
</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 增减额度
			<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="changeQuotal" onsubmit="submitonce(this);" namespace="/office" id="mainform" theme="simple">
			<table align="left" border="0">
				<!--<tr><td>订单号:</td><td><s:textfield name="billno" size="30"/></td></tr>-->
				<tr><td>玩家帐号:</td><td><s:textfield name="loginname" size="30" onblur="loadInfo();"/></td></tr>
				<tr><td>真实姓名:</td><td><s:textfield name="accountName" size="30" readonly="true"/></td></tr>
				<tr><td>用户的当前总额度:</td><td><s:textfield name="credit" size="30" disabled="true" value="%{#request.credit}"/></td></tr>
				<tr><td>增加/扣除:</td><td><s:select name="isAdd" list="#{1:'扣除',0:'增加'}" /></td></tr>
				<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market' || #session.operator.authority=='market_manager'">
              		<tr>
	              		<td>类型:</td><td><s:select name="status" list="#{'1':'真人','2':'代理老虎机佣金'}" listKey="key" listValue="value"/></td>
	              	</tr>
	            </s:if>
				<tr><td>额度:</td><td><s:textfield name="amount" size="30"  /></td></tr>
				<tr><td>备注:</td><td><s:textarea name="remark" cols="30" rows="5"/></td></tr>
				<tr><td align="center"><s:submit value="提交" /><s:reset value="重置"/></td><td></td></tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
