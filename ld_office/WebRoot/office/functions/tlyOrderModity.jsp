<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<title>同略云补单</title>
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
			操作 --&gt; 同略云补单
			<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="reSendTlyOrder"  namespace="/office" id="mainform" theme="simple">
			<table align="left" border="0">
				<tr><td>订单附言:</td><td><s:textfield name="order" size="30" /></td></tr>
				<tr><td> 额度:</td><td><s:textfield name="amount" size="30" /></td></tr>
				<tr><td align="center"><s:submit value="提交" /></td><td></td></tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
