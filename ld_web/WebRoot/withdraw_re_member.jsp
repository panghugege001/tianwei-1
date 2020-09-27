<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
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
	<form action="${withdrawRecordsUrl}" method="post" name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>序号</th>
				<th>编号</th>
				<th>提款金额</th>
				<th>提款时间</th>
				<th>状态</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}"
						status="st">
				<tr>
					<td><s:property value="#st.index+1" /></td>
					<td><s:property value="#fc.pno" /></td>
					<td class="c-red"><s:property
							value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
					<td><s:property value="#fc.tempCreateTime" /></td>
					<td><s:if test="#fc.unknowflag==4">
						执行中
					</s:if> <s:else>
						<s:property
								value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)" />
					</s:else></td>
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