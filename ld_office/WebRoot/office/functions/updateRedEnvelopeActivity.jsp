<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改红包雨活动配置</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/office/updateRedEnvelopeActivity.do" name="mainform" id="mainform" method="post">
				<table>
					<tr>
						<td>活动标题：</td>
						<td><input type="text" id="title" name="redEnvelopeActivity.title" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>活动最小红利：</td>
						<td><input type="text" id="minBonus" name="redEnvelopeActivity.minBonus" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>活动最大红利：</td>
						<td><input type="text" id="maxBonus" name="redEnvelopeActivity.maxBonus" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>领取次数：</td>
						<td><input type="text" id="times" name="redEnvelopeActivity.times" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>转入平台：</td>
						<td>红包雨账户<span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>流水倍数：</td>
						<td><input type="text" id="multiples" name="redEnvelopeActivity.multiples" /></td>
					</tr>
					<tr>
						<td>存款额要求：</td>
						<td><input type="text" id="depositAmount" name="redEnvelopeActivity.depositAmount" /></td>
					</tr>
					<tr>
						<td>开始时间（存款）：</td>
						<td><s:textfield id="depositStartTime" name="redEnvelopeActivity.depositStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /></td>
					</tr>
					<tr>
						<td>结束时间（存款）：</td>
						<td><s:textfield id="depositEndTime" name="redEnvelopeActivity.depositEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /></td>
					</tr>
					<td>红利总额：</td>
					<td><input type="text" id="betAmount" name="redEnvelopeActivity.betAmount" /></td>
					<%--<tr>
						<td>投注额要求：</td>
						<td><input type="text" id="betAmount" name="redEnvelopeActivity.betAmount" /></td>
					</tr>
					<tr>
						<td>开始时间（投注）：</td>
						<td><s:textfield id="betStartTime" name="redEnvelopeActivity.betStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /></td>
					</tr>
					<tr>
						<td>结束时间（投注）：</td>
						<td><s:textfield id="betEndTime" name="redEnvelopeActivity.betEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /></td>
					</tr>--%>
					<tr>
						<td>活动开始时间：</td>
						<td><s:textfield id="startTime" name="stringStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>活动结束时间：</td>
						<td><s:textfield id="endTime" name="stringEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" /><span style="color: red;">*</span></td>
					</tr>
					<tr>
						<td>选择平台通道</td>
						<td id="entranceTD"></td>

					</tr>
					<tr>
						<td>等级：</td>
						<td id="vipTD"></td>
					</tr>
					<tr>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button" value="提 交" id="submitBtn" /></td>
					</tr>
				</table>
				<input type="hidden" id="id" name="redEnvelopeActivity.id" />
				<input type="hidden" id="platformName" name="redEnvelopeActivity.platformName" />
				<input type="hidden" id="vip" name="redEnvelopeActivity.vip" />
			</form>
		</div>
		<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
		<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/redEnvelope.js"></script>
	</body>
</html>