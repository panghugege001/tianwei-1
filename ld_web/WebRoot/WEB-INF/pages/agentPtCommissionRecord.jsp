<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="table data-table">
	<tr>
		<th>序号
		</th>
		<th>代理账号
		</th>
		<th>平台
		</th>
		<th>数据日期
		</th>
		<th>输赢额度
		</th>
		<th>优惠额度
		</th>
		<th>洗码额度
		</th>
		<th>日佣金
		</th>
		<th>是否派发
		</th>
		<th>创建时间
		</th>
	</tr>
	<tbody>
	<c:set var="amountSum" value="0" scope="request"></c:set>
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
		<tr>
			<td>
				<s:property value="#st.index+1" />
			</td>
			<td>
				<s:property value="#fc.id.agent" />
			</td>
			<td>
				<s:if test="#fc.id.platform=='slotmachine'">老虎机佣金</s:if>
				<s:if test="#fc.id.platform=='liveall'">其他佣金</s:if>
			</td>
			<td>
				<s:property value="#fc.id.createdate" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.profitall)" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.couponfee)" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.ximafee)" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@double2String(#fc.amount)" />
			</td>
			<td>
				<s:if test="#fc.flag==1">已派发</s:if>
				<s:if test="#fc.flag==0">未派发</s:if>
			</td>
			<td>
				<s:property value="#fc.tempExcuteTime" />
			</td>
		</tr>
		
		<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
		<c:set var="amountSum" value="${amountSum+amountValue}"
			scope="request"></c:set>
	</s:iterator>
	<tr>
		<td colspan="6"></td>
		<td>
			当页总计：&nbsp;
		</td>
		<td>
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
		</td>
		<td colspan="1"></td>
	</tr>
	<tr>
		<td colspan="6"></td>
		<td>
			总计：&nbsp;
		</td>
		<td>
			<s:property
				value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
		</td>
		<td colspan="1"></td>
	</tr>
	</tbody>
</table>
<div class="pagination">
		<span class="page-info">
			共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
		</span>
		<a class="first-page" href="javaScript:void(0);" onclick="ptCommissionsRecord(1);">首页</a>
		<a class="prev-page" href="javaScript:void(0);"
			onclick="ptCommissionsRecord(${pageIndex-1});">上一页</a>
		<a class="next-page" href="javaScript:void(0);"
			onclick="ptCommissionsRecord(${pageIndex+1});">下一页</a>
		<a class="last-page" href="javaScript:void(0);"
			onclick="ptCommissionsRecord(${page.totalPages});">尾页</a>
</div>



