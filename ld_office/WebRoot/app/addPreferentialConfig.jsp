<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增自助优惠配置</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/app/addPreferentialConfig.do" name="mainform" id="mainform" method="post">
				<table>
					<tr>
						<td>游戏平台：</td>
						<td>
							<select id="platformId" name="preferentialConfig.platformId" style="width: 130px;"></select>
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>优惠类型：</td>
						<td>
							<select id="titleId" name="preferentialConfig.titleId" style="width: 150px;"></select> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>优惠标题：</td>
						<td>
							<input type="text" id="aliasTitle" name="preferentialConfig.aliasTitle" /> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>存送百分比：</td>
						<td>
							<input type="text" id="percent" name="preferentialConfig.percent" /> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>存送流水倍数：</td>
						<td>
							<input type="text" id="betMultiples" name="preferentialConfig.betMultiples" /> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>最大额度：</td>
						<td>
							<input type="text" id="limitMoney" name="preferentialConfig.limitMoney" /> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr id="limit">
						<td colspan="2">
							<table>
								<tr>
									<td>存款额要求：</td>
									<td>
										<input type="text" id="depositAmount" name="preferentialConfig.depositAmount" />
									</td>
								</tr>
								<tr>
									<td>开始时间（存款）：</td>
									<td>
										<s:textfield id="depositStartTime" name="preferentialConfig.depositStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
									</td>
								</tr>
								<tr>
									<td>结束时间（存款）：</td>
									<td>
										<s:textfield id="depositEndTime" name="preferentialConfig.depositEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
									</td>
								</tr>
								<tr>
									<td>输赢值要求：</td>
									<td>
										<input type="text" id="betAmount" name="preferentialConfig.betAmount" />
									</td>
								</tr>
								<tr>
									<td>开始时间（输赢）：</td>
									<td>
										<s:textfield id="betStartTime" name="preferentialConfig.betStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
									</td>
								</tr>
								<tr>
									<td>结束时间（输赢）：</td>
									<td>
										<s:textfield id="betEndTime" name="preferentialConfig.betEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>启用开始时间：</td>
						<td>
							<s:textfield id="startTime" name="startTimestr" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>启用结束时间：</td>
						<td>
							<s:textfield id="endTime" name="endTimestr" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>次数：</td>
						<td>
							<input type="text" id="times" name="preferentialConfig.times" size="18" />次/<select id="timesFlag" name="preferentialConfig.timesFlag"></select> 
							<span style="color: red;">*</span>
						</td>
					</tr>
					<tr>
						<td>等级：</td>
						<td id="vipTD"></td>
					</tr>
					<tr>
						<td>申请通道：</td>
						<td id="passageTD"></td>
					</tr>
					<tr>
						<td>短信反向验证是否开启：</td>
						<td>
							<label><input type='radio' name='sms' value="0" checked />否</label>
							<label><input type='radio' name='sms' value="1" />是</label>
						</td>
					</tr>
					<tr>
						<td>机器码验证是否开启：</td>
						<td>
							<label><input type='radio' name='machineEnabled' value="0" checked />否</label>
							<label><input type='radio' name='machineEnabled' value="1" />是</label>
						</td>
					</tr>
					<tr>
						<td>机器码使用次数：</td>
						<td>
							<input type="text" id="machineCodeTimes" name="preferentialConfig.machineCodeTimes" size="18" />
						</td>
					</tr>
					<tr>
						<td>最低转账金额：</td>
						<td>
							<input type="text" id="lowestAmount" name="preferentialConfig.lowestAmount" size="18" />
						</td>
					</tr>
					<tr>
						<td>最高转账金额：</td>
						<td>
							<input type="text" id="highestAmount" name="preferentialConfig.highestAmount" size="18" />
						</td>
					</tr>
					<tr>
						<td>优惠互斥组别：</td>
						<td>
							<input type="text" id="groupId" name="preferentialConfig.groupId" size="18" />
						</td>
					</tr>
					<tr>
						<td>互斥组别申请次数：</td>
						<td>
							<input type="text" id="mutexTimes" name="preferentialConfig.mutexTimes" size="18" />
						</td>
					</tr>
					<tr>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="button" value="提 交" id="submitBtn" />
						</td>
					</tr>
				</table>
				<input type="hidden" id="platformName" name="preferentialConfig.platformName" />
				<input type="hidden" id="titleName" name="preferentialConfig.titleName" /> 
				<input type="hidden" id="vip" name="preferentialConfig.vip" /> 
				<input type="hidden" id="isPhone" name="preferentialConfig.isPhone" /> 
				<input type="hidden" id="isPassSms" name="preferentialConfig.isPassSms" />
				<input type="hidden" id="machineCodeEnabled" name="preferentialConfig.machineCodeEnabled" />
			</form>
		</div>
		<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
		<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctx}/app/common/preferential.js"></script>
	</body>
</html>