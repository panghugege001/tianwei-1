<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
	<jsp:include page="/tpl/checkUser.jsp"></jsp:include>

	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>

<body>
<div class="dialog-cnt">
	<s:url action="queryBetOfPlatform" namespace="/asp" var="betOfPlatformUrl"></s:url>
	<s:url action="queryBetOfPlatformWeek" namespace="/asp" var="betOfPlatformWeekUrl"></s:url>
	<ul class="tab-hd">
		<li><a href="${betOfPlatformUrl}">本月投注额</a></li>
		<li class="active"><a href="${betOfPlatformWeekUrl}">本周投注额</a></li>
	</ul>
</div>

<div class="data-list">
	<table class="table data-table">
		<thead>
		<tr>
			<th>编号</th>
			<th>游戏平台</th>
			<th>投注额</th>
		</tr>
		</thead>
		<tbody>
		<s:iterator var="fc" value="%{#request.bets}" status="st">
			<tr>
				<td><s:property value="#st.index+1" /></td>
				<td><s:property value="#fc.platform" /></td>
				<td><s:property
						value="@dfh.utils.NumericUtil@formatDouble(#fc.bet)" /></td>
			</tr>
		</s:iterator>
		</tbody>

	</table>
	<div class="ipt-group text-center">
		<input type="button" class="btn btn-pay btn-jinji" onclick="checkUpgrade('week')" value="检测升级" />
	</div>

</div>

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
	//处理升级
	function checkUpgrade(type) {
		//closeProgressBar();
		$.ajax({
			type : "post",
			url : "${ctx}/asp/checkUpgrade.aspx",
			cache : false,
			data : {
				"type" : type
			},
			success : function(data) {
				alert(data);
			},
			error : function() {
				alert("系统错误");
			},
			complete : function() {
				//closeProgressBar();
			}
		});
	}
</script>
</body>
</html>