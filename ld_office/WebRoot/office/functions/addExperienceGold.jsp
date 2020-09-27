<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>


<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		
<table >
<tr><td>优惠类型</td><td>

<select id="title" name="title" onchange="displayTr(this.value);">
    <option value="自助体验金">自助体验金</option>
	<option value="APP下载彩金">APP下载彩金</option>
</select><span style="color: red;">*</span> </td></tr>
<tr></tr>

<tr><td>游戏平台 </td><td>
<select id="platformName" name="platformName" >
</select>
<span style="color: red;">*</span></td></tr>

<tr><td>标题 </td><td><input type="text" id="aliasTitle" name="aliasTitle" required="required"/><span style="color: red;">*</span></td></tr>
<tr>
	<td>体验金额度：</td>
	<td>
		<input name="amount" id="amount"/><span style="color: red;">*</span>
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
		<input type="text" id="machineCodeTimes" name="machineCodeTimes" size="18" />
	</td>
</tr>
<tr><td>限制最大金额</td><td><input name="maxMoney" id="maxMoney" value="100"/></td></tr>
<tr><td>限制最小金额</td><td><input name="minMoney" id="minMoney" value="1"/></td></tr>
<tr><td>启用开始时间</td><td><s:textfield name="starttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
<tr><td>启用结束时间</td><td><s:textfield name="endtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
<tr><td>次数</td><td><input type="text" name="times" id="times"/>次/<select name="timesflag"><option value="1">日</option><option value="2">周</option><option value="3">月</option><option value="4">年</option></select></td></tr>
<tr><td>等级</td>

<td id="vipTD">
<!-- <label><input type="checkbox"  onclick='checkAll(this)' >全选</label> -->
<%-- <label><input type="checkbox" name="viplevel" value="0" ><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label> --%>
<%-- <label><input type="checkbox" name="viplevel" value="1" ><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label> --%>
<%-- <label><input type="checkbox" name="viplevel" value="2" ><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label> --%>
<%-- <label><input type="checkbox" name="viplevel" value="3" ><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label> --%>
<%-- <label><input type="checkbox" name="viplevel" value="4" ><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label> --%>
<%-- <label><input type="checkbox" name="viplevel" value="5" ><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label> --%>
</td></tr>
<tr><td>申请通道</td><td>
<label><input type='checkbox' onclick='checkAllPassage(this)' />全选</label>
<label><input type="checkbox" name="isPhoneData" value="1" >官网</label>
<label><input type="checkbox" name="isPhoneData" value="2" >WEB</label>
<label><input type="checkbox" name="isPhoneData" value="3" >安卓APP</label>
<label><input type="checkbox" name="isPhoneData" value="4" >苹果APP</label>
<input type="hidden" id="vip" name="vip"/>
<input type="hidden" id="machineCodeEnabled" name="machineCodeEnabled" />
<input type="hidden" id="isPhone" name="isPhone"/>
</td></tr>
<tr><td align="center"><input type="button" value="创建" onclick="create()"/></td><td></td></tr>
</table>
	</s:form>
</div>
<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
<script type="text/javascript" src="${ctx}/app/common/experienceGold.js"></script>