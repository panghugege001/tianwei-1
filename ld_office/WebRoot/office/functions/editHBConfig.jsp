<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!-- <script type="text/javascript" src="/js/prototype_1.6.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>


<script type="text/javascript">
$(document).ready(function(){
	$("#starttime").val($("#starttimeTmp").val());
	$("#endtime").val($("#endtimeTmp").val());
});

function update(){
	var str="";
	$("input[name='viplevel']:checked").each(function(){   
	     str+=$(this).val();   
	 });
	$("#vip").val(str);
	if(confirm("确定？")){
		var data = $("#mainform").serialize();
		var action = "/office/updateHBConfig.do";
		$.ajax({
			  url:action,
			  type:"POST",
			  data:data,
			  contentType:"application/x-www-form-urlencoded; charset=utf-8",
			  success: function(resp){
				  alert(resp);
			  },
			  complete:responseMethod
			});
	}
}
function responseMethod(data){
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
	window.close();
}
</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<input type="hidden" name="id" value="${hbConfig.id}">
		<input type="hidden" id="starttimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="hbConfig.starttime" />">
		<input type="hidden" id="endtimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="hbConfig.endtime" />">
		<table>
			<tr>
				<td>标题：</td>
				<td><input name="title" value="${hbConfig.title}" /></td>
			</tr>
			<tr class="cunsong">
				<td>存款下限金额（包含）</td>
				<td><input type="text" name="limitStartMoney"
					value="${hbConfig.limitStartMoney}" /></td>
			</tr>
			<tr class="cunsong">
				<td>存款上限金额（不包含）</td>
				<td><input type="text" name="limitEndMoney"
					value="${hbConfig.limitEndMoney}" /></td>
			</tr>
			<%-- <tr class="cunsong">
				<td>流水倍数</td>
				<td><input type="text" name="betMultiples"
					value="${hbConfig.betMultiples}" /></td>
			</tr> --%>
			<tr class="amount">
				<td>红包金额</td>
				<td><input name="amount" value="${hbConfig.amount}"/></td>
			</tr>
			<tr>
				<td>启用开始时间</td>
				<td><s:textfield name="starttime" id="starttime"
						size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						My97Mark="false" cssClass="Wdate" /></td>
			</tr>
			<tr>
				<td>启用结束时间</td>
				<td><s:textfield name="endtime" id="endtime"
						size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						My97Mark="false" cssClass="Wdate" /></td>
			</tr>

			<tr>
				<td>次数</td>
				<td><input type="text" name="times" value="${hbConfig.times}" />次/
					<select name="timesflag">
						<option value="1"
							<s:if test="hbConfig.timesflag==1"> selected="selected" </s:if>>天</option>
						<option value="2"
							<s:if test="hbConfig.timesflag==2"> selected="selected" </s:if>>周</option>
						<option value="3"
							<s:if test="hbConfig.timesflag==3"> selected="selected" </s:if>>月</option>
						<option value="4"
							<s:if test="hbConfig.timesflag==4"> selected="selected" </s:if>>年</option>
					</select>
				</td>
			</tr>

			<tr>
				<td>等级</td>
				<td>
					<label><input type="checkbox" name="viplevel" value="0" <s:if test="hbConfig.vip.contains(\"0\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label>
					<label><input type="checkbox" name="viplevel" value="1" <s:if test="hbConfig.vip.contains(\"1\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label>
					<label><input type="checkbox" name="viplevel" value="2" <s:if test="hbConfig.vip.contains(\"2\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label>
					<label><input type="checkbox" name="viplevel" value="3" <s:if test="hbConfig.vip.contains(\"3\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label>
					<label><input type="checkbox" name="viplevel" value="4" <s:if test="hbConfig.vip.contains(\"4\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label>
					<label><input type="checkbox" name="viplevel" value="5" <s:if test="hbConfig.vip.contains(\"5\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label>
					<label><input type="checkbox" name="viplevel" value="6" <s:if test="hbConfig.vip.contains(\"6\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(6)"/></label>
					<label><input type="checkbox" name="viplevel" value="7" <s:if test="hbConfig.vip.contains(\"7\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(7)"/></label>
					<label><input type="checkbox" name="viplevel" value="8" <s:if test="hbConfig.vip.contains(\"8\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(8)"/></label>
					<input type="hidden" id="vip" name="vip"/>
				</td>
			</tr>
			<tr>
				<td align="center"><input type="button" value="更新"
					onclick="update();" /></td>
				<td></td>
			</tr>

		</table>
	</s:form>
</div>