<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!-- <script type="text/javascript" src="/js/prototype_1.6.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<input type="hidden" name="id" value="${experienceGoldConfig.id}">
		<input type="hidden" id="vipTmp" name="vipTmp" value="${experienceGoldConfig.vip}">
		<input type="hidden" id="machineCodeEnabled" name="machineCodeEnabled" value="${experienceGoldConfig.machineCodeEnabled}" />
		<input type="hidden" id="platformNameTmp" name="platformNameTmp" value="${experienceGoldConfig.platformName}" />
		<input type="hidden" id="starttimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="experienceGoldConfig.startTime" />">
		<input type="hidden" id="endtimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="experienceGoldConfig.endTime" />">
<table >
	<tr><td>优惠类型：</td>
	<td><input name="title" id="title" value="${experienceGoldConfig.title}" readonly="readonly"/></td>
	</tr>
	<tr><td>游戏平台 </td><td>
		<select id="platformName" name="platformName" >
		</select>
		<span style="color: red;">*</span></td>
	</tr>
	<tr>
		<td>体验金额度：</td>
		<td>
			<input name="amount" id="amount" value="${experienceGoldConfig.amount}" />
		</td>
	</tr>	
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
			<input type="text" id="machineCodeTimes" name="machineCodeTimes" value="${experienceGoldConfig.machineCodeTimes}" size="18" />
		</td>
	</tr>
	<tr><td>标题</td><td><input type="text" name="aliasTitle"  id="aliasTitle" value="${experienceGoldConfig.aliasTitle}" /></td></tr>
	<tr><td>限制最大金额</td><td><input name="maxMoney" id="maxMoney"  value="${experienceGoldConfig.maxMoney}" /></td></tr>
	<tr><td>限制最小金额</td><td><input name="minMoney" id="minMoney"  value="${experienceGoldConfig.minMoney}" /></td></tr>
	<tr><td>启用开始时间</td><td><s:textfield name="starttime" id="startTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
	<tr><td>启用结束时间</td><td><s:textfield name="endtime" id="endTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
	
	<tr><td>次数</td><td><input type="text" name="times" id="times" value="${experienceGoldConfig.times}"/>次/
	
	<select name="timesflag"  >
		<option value="1" <s:if test="experienceGoldConfig.timesFlag==1"> selected="selected" </s:if>>天</option>
		<option value="2" <s:if test="experienceGoldConfig.timesFlag==2"> selected="selected" </s:if>>周</option>
		<option value="3" <s:if test="experienceGoldConfig.timesFlag==3"> selected="selected" </s:if>>月</option>
		<option value="4" <s:if test="experienceGoldConfig.timesFlag==4"> selected="selected" </s:if>>年</option>
	</select>
	</td></tr>
	
	<tr><td>等级</td><td id="vipTD">
	</td></tr>
	<tr><td>申请通道</td><td>
	<label><input type='checkbox' onclick='checkAllPassage(this)' />全选</label>
	<label><input type="checkbox" name="isPhoneData" <s:if test="experienceGoldConfig.isPhone.contains(\"1\")">checked="checked"</s:if> value="1" >官网</label>
	<label><input type="checkbox" name="isPhoneData" <s:if test="experienceGoldConfig.isPhone.contains(\"2\")">checked="checked"</s:if>value="2" >WEB</label>
	<label><input type="checkbox" name="isPhoneData" <s:if test="experienceGoldConfig.isPhone.contains(\"3\")">checked="checked"</s:if>value="3" >安卓APP</label>
	<label><input type="checkbox" name="isPhoneData" <s:if test="experienceGoldConfig.isPhone.contains(\"4\")">checked="checked"</s:if>value="4" >苹果APP</label>
	<input type="hidden" id="vip" name="vip"/>
	<input type="hidden" id="isPhone" name="isPhone"/>
	</td></tr>
	<tr><td align="center"><input type="button" value="更新" onclick="update();"/></td><td></td></tr>

</table>
	</s:form>
</div>
<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
<script type="text/javascript" src="${ctx}/app/common/experienceGold.js"></script>