<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table id="agentPlatformRecordTable" class="table data-table">
<thead>
	<tr>
		<th width="10%">
			<strong>序号</strong>
		</th>
		<th width="20%">
			<strong>会员账号</strong>
		</th>
		<th width="20%">
			<strong>创建时间</strong>
		</th>
		<th width="10%">
			<strong>平台</strong>
		</th>
		<th width="15%">
			<strong>投注总额</strong>
		</th>
		<th width="15%">
			<strong>输赢值</strong>
		</th>
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
				<s:property value="#fc.tempCreateTime" />
			</td>
			<td>
				<s:property value="#fc.platform" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.bettotal)" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.amount)" />
			</td>
		</tr>
		<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
		<c:set var="amountSum" value="${amountSum+amountValue}"
			scope="request"></c:set>
			
		<s:set var="betValue" value="#fc.bettotal" scope="request"></s:set>
		<c:set var="betSum" value="${betSum+betValue}"
			scope="request"></c:set>
	</s:iterator>
	<tr>
		<td colspan="4"></td>
		<td>
			投注当页总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.betSum)" />
		</td>
		<td>
			输赢当页总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
		</td>
	</tr>
	<tr>
		<td colspan="4"></td>
		<td>
			投注总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
		</td>
		<td>
			输赢总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
		</td>
	</tr>
	</tbody>
</table>
<div class="pagination">
	<span class="page-info">
		共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
	</span>
	<a href="javaScript:void(0);" onclick="agentPlatformRecordTwo(1);"  class="first-page">首页</a>
	<a href="javaScript:void(0);"
		onclick="agentPlatformRecordTwo(${pageIndex-1});"  class="prev-page">上一页</a>
	<a href="javaScript:void(0);"
		onclick="agentPlatformRecordTwo(${pageIndex+1});"  class="next-page">下一页</a>
	<a href="javaScript:void(0);"
		onclick="agentPlatformRecordTwo(${page.totalPages});"  class="last-page">尾页</a>
</div>



