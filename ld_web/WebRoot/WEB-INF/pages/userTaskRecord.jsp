<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>  
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=request.getRequestURL()%>" />
<title>龙都娱乐城--明细查询</title>
<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>

<body>	
<div class="data-list">
	<form action="${queryTaskRecords}" method="post" data-dataform name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>编号</th>
				<th>标题</th>
				<th>历史投注</th>
				<th>状态</th>
				<th>创建时间</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td><s:property value="#st.index+1"/></td>
					<td><s:property value="#fc.title"/></td>
					<td class="c-red"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.historyBet)"/></td>
					<td>
						<s:if test="#fc.isAdd==0">未完成</s:if>
						<s:if test="#fc.isAdd==1">成功</s:if>
						<s:if test="#fc.isAdd==2">过期</s:if>
					</td>
					<td class="type"><s:property value="#fc.tmpCreatetime"/></td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
		<div class="pagination" >
			<input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
			<input type="hidden" name="size" value="10" />
			${page.jsPageCode}
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