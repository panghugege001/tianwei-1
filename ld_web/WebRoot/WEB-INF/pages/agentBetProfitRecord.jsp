<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="agentPlatformRecordTable" class="table data-table">
<thead>
	<tr>
		<th width="10%">序号</th>
		<th width="10%">会员账号</th>
		<th width="10%">平台</th>
		<th width="15%">投注总额</th>
		<th width="15%">输赢值</th>
		<th width="15%">起始时间</th>
		<th width="15%">结束时间</th>
	</tr>
</thead>
<tbody>
	<c:set var="amountSum" value="0" scope="request"></c:set>
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
		<tr>
			<td>
				<s:property value="#st.index+1" />
			</td>
			<td>
				<s:property value="#fc.loginname" />
			</td>
			<td>
				<s:property value="#fc.platform" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.bet)" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.profit)" />
			</td>
			<td>
				<s:property value="@dfh.utils.DateUtil@fmtDateForBetRecods(#fc.tempStarttime)" />
			</td>
			<td>
				<s:property value="@dfh.utils.DateUtil@fmtDateForBetRecods(#fc.tempEndtime)" />
			</td>
		</tr>
		<s:set var="betValue" value="#fc.bet" scope="request"></s:set>
		<c:set var="betSum" value="${betSum+betValue}"
			scope="request"></c:set>
			
		<s:set var="profitValue" value="#fc.profit" scope="request"></s:set>
		<c:set var="profitSum" value="${profitSum+profitValue}"
			scope="request"></c:set>
	</s:iterator>
	<tr>
		<td colspan="3"></td>
		<td>
			当页总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.betSum)" />
		</td>
		<td>
			当页总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.profitSum)" />
		</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<td colspan="3"></td>
		<td>
			总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
		</td>
		<td>
			总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
		</td>
		<td colspan="2"></td>
	</tr>
	</tbody>
</table>

<div class="pagination">
        <span class="page-info">
            共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
        </span>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(1);"  class="first-page">首页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex-1});"  class="prev-page">上一页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex+1});"  class="next-page">下一页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${page.totalPages});"  class="last-page">尾页</a>
</div>



