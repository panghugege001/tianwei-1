<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp"%>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<div id="excel_menu">
	<table>
		<tr>
			<td>
				提案号:
			</td>
			<td>
				${propo.pno}
			</td>
		</tr>
		<tr>
			<td>
				会员帐号:
			</td>
			<td>
			${propo.loginname}
			</td>
		</tr>
		<tr>
			<td>
				存款金额:
			</td>
			<td>
			${propo.amount}
			</td>
		</tr>
		
		<tr>
			<td>
				赠送金额:
			</td>
			<td>
			${propo.gifTamount}
			</td>
		</tr>
		
			<tr>
			<td>
				投注倍数:
			</td>
			<td>
			${propo.betMultiples}
			</td>
		</tr>
		<tr>
			<td>
				备注:
			</td>
			<td>
			   ${propo.shippinginfo}
			</td>
		</tr>
	</table>
</div>
