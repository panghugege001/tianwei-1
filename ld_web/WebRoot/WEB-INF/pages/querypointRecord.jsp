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
<form action="" method="post" data-dataform="" name="mainform">
	<div class="data-list">
		<table class="table data-table">
			<tr>
				<th>
					序号
				</th>
				<th>
					积分值
				</th>
				<th>
					积分类型
				</th>
				<th>
					时间
				</th>
			</tr>
			<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td>
						<s:property value="#st.index+1" />
					</td>
					<td>
						<s:property value="#fc.points" />
					</td>
					<td>
						<s:property value="#fc.type" />
					</td>
					<td>
						<s:property value="#fc.createday" />
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>


	<s:if test="pageIndex>=#request.page.totalPages">
		<div class="pagination">共${page.totalRecords}条 每页${size}条 当前${page.totalPages}/${page.totalPages}
			<a href="javaScript:void(0);" onclick="chagepage(1);" class="btn-page">首页</a>
			<a href="javaScript:void(0);" onclick="chagepage(${pageIndex-1});" class="btn-page">上一页</a>
			<a href="javaScript:void(0);" class="btn-page">下一页</a>
			<a href="javaScript:void(0);" onclick="chagepage(${page.totalPages});" class="btn-page">尾页</a></div>
	</s:if>
	<s:else>
		<div class="pagination">共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
			<a href="javaScript:void(0);" onclick="chagepage(1);" class="btn-page">首页</a>
			<a href="javaScript:void(0);" onclick="chagepage(${pageIndex-1});" class="btn-page">上一页</a>
			<a href="javaScript:void(0);" onclick="chagepage(${pageIndex+1});" class="btn-page">下一页</a>
			<a href="javaScript:void(0);" onclick="chagepage(${page.totalPages});" class="btn-page">尾页</a></div>
	</s:else>


	<input type="hidden" name="pageIndex" value="1" id="pageIndex" />
	<input type="hidden" name="size" value="10" />
</form>

<script type="text/javascript">
	function chagepage(val) {
		if (val <= 1) {
			val = 1;
		}
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	}
</script>
</body>


