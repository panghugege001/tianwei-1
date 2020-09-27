<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<table class="table">
	<tr>
		<td><strong>输赢金额</strong></td>
		<td><strong>扣除红利</strong></td>
		<td><strong>活动礼金</strong></td>
		<td><strong>派发时间</strong></td>
		<td><strong>操作</strong></td>
	</tr>
	<s:iterator var="fc" value="%{#request.bonusList}" status="st">
	<tr>
		<td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.netWinOrLose)"/></td>
		<td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.bonus)"/></td>
		<td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.giftMoney)"/></td>
		<td><s:property value="#fc.tempDistributeTime"/></td>
		<td>
			<s:if test="#fc.status==1">
				<a href="javascript:getPTBigBangBonus(<s:property value="#fc.id"/>)" class="btn btn-sm btn-linqu">领取</a>
			</s:if>
		</td>
	</tr>
	</s:iterator>
	<tr>
		<td colspan="5"><span class="c-red">注意：请在派发后24小时内领取活动礼金，否则将自动失效</span></td>
	</tr>
</table>