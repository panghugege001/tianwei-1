<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@page import="dfh.action.vo.QueryDataEuroVO" %>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<style>
			html,body{
				height:auto;
			}
			.table { width: 100%; color: #af98ac; font-size: 16px;margin-top: 32px; box-sizing: border-box;}
			.table th {padding:0;background:none;font-size: 16px;font-weight: bold}
			.table td { background: #432d40;padding:0; background: none;}
			.table thead { background-color: #432d40; line-height: 52px; }
			.table thead th { text-align: left;width: 33.3%; }
			.table thead th:first-child { padding-left: 48px; }
			.table tbody tr:nth-child(odd) { background-color: #332431!important; }
			.table tbody tr:nth-child(even) { background-color: #2d202b!important; }
			.table tbody td { width: 33.3%; line-height: 44px; text-align: left; }
			.table tbody td:first-child { padding-left: 50px; }
			.pagination { text-align: right;color: #af98ac;padding:0;}
			.table th, .table td {border: none;}
			.table tr:nth-child(even) td{background: #2d202b;}
		</style>
	</head>
	<body>
	<form action="${depositRecordsUrl}" method="post" name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>排名</th>
				<th>游戏账号</th>
				<th>抢票时间</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td><s:property value="#st.index+1" /></td>
					<td><s:property value="#fc.loginname" /></td>
					<td><s:property value="#fc.createtime" /></td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
		<div class="pagination">
			<input type="hidden" name="pageIndex" value="1" id="pageIndex" />
			<input type="hidden" name="size" value="10" />
			${page.jsPageCode}
		</div>
	</form>
	</body>
	<script type="text/javascript">
		
		function gopage(val) {
			
			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		};
	</script>
</html>