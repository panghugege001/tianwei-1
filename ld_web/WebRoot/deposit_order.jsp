<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
	<jsp:include page="/tpl/checkUser.jsp"></jsp:include>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
       <style type="text/css">
		.data-list { padding-top:10px; }
		.table { width: 98%; margin: 0 10px 10px; border: 1px solid #bababa; }
		.table tr th { color:#fff; }
		.table tr:nth-child(even) { background-color:#f0f0f0; }
		.table td { background:none; border:none; color: #342923; }
		</style>
</head>
<body>
<div class="data-list">
	<form action="${depositOrderRecord}" method="post" name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>序号</th>
				<th>附言</th>
				<th>银行</th>
				<th>账号名</th>
				<th>备注</th>
				<th>创建时间</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}"
						status="st">
				<tr>
					<td><s:property value="#st.index+1" /></td>
					<td><s:property value="#fc.depositId" /></td>
					<td><s:property value="#fc.bankname" /></td>
					<td><s:property value="#fc.accountname" /></td>
					<td><s:property value="#fc.remark" /></td>
					<td><s:property value="#fc.tempCreateTime" /></td>
					<td><s:if test="#fc.status==0">待处理</s:if>
						<s:elseif test="#fc.status==1">成功</s:elseif>
						<s:elseif test="#fc.status==2">支付失败</s:elseif>
					</td>
					<td><s:if test="#fc.status==0"><input type="button" id = "<s:property value="#fc.depositId" />" class="btn btn-sm" value="禁止操作" disabled="disabled"> </s:if>
						<s:elseif test="#fc.status==1"><input type="button" id = "<s:property value="#fc.depositId" />" class="btn btn-sm" value="禁止操作" disabled="disabled"></s:elseif>
						<s:elseif test="#fc.status==2"><input type="button" id = "<s:property value="#fc.depositId" />" class="btn btn-sm" value="禁止操作" disabled="disabled"></s:elseif>
					</td>
				</tr>
			</s:iterator>
			</tbody>

		</table>
		<div class="pagination">
			<input type="hidden" name="pageIndex" value="1" id="pageIndex" />
			<input type="hidden" name="size" value="10" /> ${page.jsPageCode}
		</div>
	</form>

</div>

	<script type="text/javascript">
		function gopage(val) {
			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		}
		function cancelDeposit(deposit) {
			<%--var depositId = deposit.id;--%>
            <%--if(depositId == ''){--%>
            <%--alert('未获取到有效附言ID，请您刷新页面重新操作！');--%>
            <%--}--%>
            <%--$.post("${ctx}/asp/cancelNewdeposit.aspx", {--%>
            <%--"depositId":depositId--%>
            <%--}, function (returnedData, status) {--%>
            <%--if ("success" == status) {--%>
            <%--var result = returnedData;--%>
            <%--if (result == "success") {--%>
            <%--alert("废除订单成功");--%>
            <%--$("#" + depositId).val("禁止操作");--%>
            <%--$("#" + depositId).attr("disabled", "disabled"); --%>
            <%--} else {--%>
            <%--alert(result);--%>
            <%--}--%>
            <%--}--%>
            <%--});--%>
		}
	</script>
</body>
</html>