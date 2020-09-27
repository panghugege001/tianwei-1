<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>  
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<style>
		html,body{
			height: auto;
		}
	</style>
</head>

<body>	
<div class="section">
    <s:url action="querybetRank" namespace="/asp" var="querybetRank"></s:url>
	<form action="${querybetRank}" method="post" data-dataform name="mainform">
		<table class="table">
			<thead>
			<tr>
				<th>玩家账号</th>
				<th>游戏平台</th>
				<th>玩家流水</th>
				<th>所在地区</th>
				<th>玩家排名</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td><s:property value="#fc.loginname"/></td>
					<td><s:property value="#fc.platform"/></td>
					<td class="c-bold"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
					<td><s:property value="#fc.address"/></td>
					<td><s:property value="#fc.no"/></td>
				</tr>
			</s:iterator>
			</tbody>

		</table>
		<div class="pagination" >
			<input type="hidden" name="type" value="ttg"/>
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