<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
	<title>天威娱乐城--明细查询</title>

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
	<form action="${couponRecordsUrl }" method="post" name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>编号</th>
				<th>类型</th>
				<th>存款</th>
				<th>赠送</th>
				<th>倍数</th>
				<th>代码</th>
				<th>转账</th>
				<th>执行时间</th>

			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}"
						status="st">
				<tr>
					<td class="text-left"><s:property value="#fc.pno" /></td>
					<td><s:property
							value="@dfh.model.enums.ProposalType@getText(#fc.type)" /></td>
					<td class="c-red"><s:property
							value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
					<td><s:property
							value="@dfh.utils.NumericUtil@formatDouble(#fc.gifTamount)" /></td>
					<td><s:property value="#fc.betMultiples" /></td>
					<td><s:property value="#fc.shippingCode" /></td>
					<td><s:property value="#fc.remark" /></td>
					<td><s:property value="#fc.tempCreateTime" /></td>
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
</script>
</body>
</html>