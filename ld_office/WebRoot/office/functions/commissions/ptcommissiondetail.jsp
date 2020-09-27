<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>

<style type="text/css">

.tdCss{
align:center;
text-align: center;
}

</style>

<table  width="1100px" border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
<tr><th>账号</th><th>平台</th><th>输赢</th></tr>
<s:iterator var="fc" value="%{#request.list}">
	<tr>
	<td class="tdCss">${fc.id.agent}</td>
	<td class="tdCss">${fc.id.platform}</td>
	<td class="tdCss">${fc.profitall}</td>
	</tr>
</s:iterator>
</table>
