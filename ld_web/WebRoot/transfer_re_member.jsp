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
	<form action="${transferRecordsUrl}" method="post" name="mainform">
		<table class="table data-table">
			<thead>
			<tr>
				<th>序号</th>
				<th>编号</th>
				<th>转账金额</th>
				<th>转账时间</th>
				<th>转账类型</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td><s:property value="#st.index+1" /></td>
					<td><s:property value="#fc.id" /></td>
					<td class="c-red"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.remit)" /></td>
					<td><s:property value="#fc.tempCreateTime" /></td>
					<td class="type">
						<s:if test="#fc.source=='ea'">从ea账户转至天威账户 </s:if>
						<s:elseif test="#fc.paymentid!=null"><s:property value="#fc.paymentid"></s:property></s:elseif>
						<s:elseif test="#fc.source=='ag'">从ag账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='agin'">从agin账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='keno'">从keno账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='keno2'">从keno2账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='bbin'">从bbin账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='newpt'">从pt账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='USER'">红包雨转给好友</s:elseif>
						<s:elseif test="#fc.target=='REDRAIN'">主账户转到红包雨账户</s:elseif>
						<s:elseif test="#fc.source=='REDRAIN'&&#fc.target!='USER'">红包雨转入游戏平台</s:elseif>
						<s:elseif test="#fc.source=='TOUSER'">收到好友转来红包</s:elseif>
						<s:elseif test="#fc.source=='sb'">从体育账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='jc'">从时时彩账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='gpi'">从GPI账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='ttg'">从TTG账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='sixlottery'">从sixlottery账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='ebet'">从ebet账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='nt'">从nt账户转至天威账户</s:elseif>
						<s:elseif test="#fc.source=='n2live'">从N2 Live账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='ag'">从天威账户转至ag账户</s:elseif>
						<s:elseif test="#fc.target=='agin'">从天威账户转至agin账户</s:elseif>
						<s:elseif test="#fc.target=='bbin'">从天威账户转至bbin账户</s:elseif>
						<s:elseif test="#fc.target=='keno'">从天威账户转至keno账户</s:elseif>
						<s:elseif test="#fc.target=='keno2'">从天威账户转至keno2账户</s:elseif>
						<s:elseif test="#fc.target=='sb'">从天威账户转至体育账户</s:elseif>
						<s:elseif test="#fc.target=='newpt'">从天威账户转至pt账户</s:elseif>
						<s:elseif test="#fc.target=='jc'">从天威账户转至时时彩账户</s:elseif>
						<s:elseif test="#fc.target=='gpi'">从天威账户转至GPI账户</s:elseif>
						<s:elseif test="#fc.target=='ttg'">从天威账户转至TTG账户</s:elseif>
						<s:elseif test="#fc.target=='sixlottery'">从天威账户转至sixlottery账户</s:elseif>
						<s:elseif test="#fc.target=='ebet'">从天威账户转至ebet账户</s:elseif>
						<s:elseif test="#fc.target=='nt'">从天威账户转至nt账户</s:elseif>
						<s:elseif test="#fc.target=='n2live'">从天威账户转至N2 Live账户</s:elseif>
						<s:elseif test="#fc.source=='qt'">从qt账号转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='qt'">从天威账户转至qt账号</s:elseif>
						<s:elseif test="#fc.source=='ebetapp'">从EBet真人账号转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='ebetapp'">从天威账户转至EBet真人账号</s:elseif>
						<s:elseif test="#fc.source=='dt'">从DT账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='dt'">从天威账户转至DT账户</s:elseif>
						<s:elseif test="#fc.source=='mg'">从MG转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='mg'">从天威账户转至MG账户</s:elseif>
						<s:elseif test="#fc.source=='png'">从PNG转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='png'">从天威账户转至PNG账户</s:elseif>
						<s:elseif test="#fc.source=='sba'">从沙巴体育转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='sba'">从天威账户转至沙巴体育账户</s:elseif>
						<s:elseif test="#fc.source=='mwg'">从mwg账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='mwg'">从天威账户转至mwg账户</s:elseif>
						<s:elseif test="#fc.source=='slot'">从老虎机账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='slot'">从天威账户转至老虎机账户</s:elseif>
						<s:elseif test="#fc.source=='chess'">从棋乐游账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='chess'">从天威账户转至棋乐游账户</s:elseif>
						<s:elseif test="#fc.source=='pb'">从平博体育账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='pb'">从天威账户转至平博体育账户</s:elseif>
						<s:elseif test="#fc.source=='bit'">从比特账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='bit'">从天威账户转至比特账户</s:elseif>
						<s:elseif test="#fc.source=='kyqp'">从开元棋牌账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='kyqp'">从天威账户转至开元棋牌账户</s:elseif>
						<s:elseif test="#fc.source=='vr'">从VR彩票账户转至天威账户</s:elseif>
						<s:elseif test="#fc.target=='vr'">从天威账户转至VR彩票账户</s:elseif>
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
</script>
</body>
</html>