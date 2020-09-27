<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table>
	<tr>
		<td width="15%">
			<strong>序号</strong>
		</td>
		<td width="20%">
			<strong>会员账号</strong>
		</td>
		<td width="20%">
			<strong>创建时间</strong>
		</td>
		<td width="20%">
			<strong>额度</strong>
		</td>
		<td width="15%">
			<strong>提案类型</strong>
		</td>
	</tr>
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
				<c:if test="${proposalType != null && proposalType==1000}">
					<s:property value="@dfh.utils.NumericUtil@double2String(#fc.money)" />
					<s:set var="amountValue" value="#fc.money" scope="request"></s:set>
					<c:set var="amountSum" value="${amountSum+amountValue}"
						scope="request"></c:set>
				</c:if>
				<c:if test="${proposalType!=1000}">
					<s:property
						value="@dfh.utils.NumericUtil@double2String(#fc.amount)" />
					<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
					<c:set var="amountSum" value="${amountSum+amountValue}"
						scope="request"></c:set>
				</c:if>
			</td>
			<td>
				<c:if test="${proposalType != null && proposalType==1000}">
		          在线支付
	        </c:if>
				<c:if test="${proposalType!=1000}">
					<s:property value="@dfh.model.enums.ProposalType@getText(#fc.type)" />
				</c:if>
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="3"></td>
		<td>
			当前小2计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="3"></td>
		<td>
			总计：&nbsp;
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
		</td>
		<td></td>
	</tr>
</table>

<input type="hidden" id="totalpage" value="${page.totalPages}">
<div class="pagination">
        <span class="page-info">
            共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
        </span>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(1);"  class="first-page">首页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex-1});"  class="prev-page">上一页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex+1});"  class="next-page">下一页</a>
	<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${page.totalPages});"  class="last-page">尾页</a>
</div>



