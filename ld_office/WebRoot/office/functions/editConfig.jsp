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
		var action = "/office/updateYouHuiConfig.do";
		$.ajax({
			  url:action,
			  type:"POST",
			  data:data,
			  contentType:"application/x-www-form-urlencoded; charset=utf-8",
			  success: function(resp){
				  alert(resp);
			  }
			});
	}
}

</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<input type="hidden" name="id" value="${youhuiConfig.id}">
		<input type="hidden" id="starttimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="youhuiConfig.starttime" />">
		<input type="hidden" id="endtimeTmp" value="<s:date format="yyyy-MM-dd HH:mm:ss" name="youhuiConfig.endtime" />">
		<table >
<tr><td>优惠类型：</td><td><input name="title" value="${youhuiConfig.title}" disabled="disabled"/>
 </td></tr>
<tr></tr>
</table>
<table >
<tr class="cunsong"><td>百分比</td><td><input type="text" name="percent" value="${youhuiConfig.percent}"  disabled="disabled"/></td></tr>
<tr></tr>
<tr class="cunsong"><td>流水倍数</td><td><input type="text" name="betMultiples"  value="${youhuiConfig.betMultiples}" disabled="disabled"/></td></tr>
<tr></tr>
<tr class="cunsong"><td>最大额度</td><td><input type="text" name="limitMoney"  value="${youhuiConfig.limitMoney}" disabled="disabled"/></td></tr>
<tr></tr>

<tr class="tiyanjin" style="display: none;"><td>体验金额度</td><td><input name="amount" value="${youhuiConfig.amount}"   disabled="disabled"/></td></tr>
<tr></tr>
</table>

<table >
<tr><td>标题</td><td><input type="text" name="aliasTitle"  value="${youhuiConfig.aliasTitle}" /></td></tr>
<c:if test = "${'APP下载彩金'==youhuiConfig.aliasTitle}">  
	<tr><td>彩金金额</td><td><input type="text" name="amount"  value="${youhuiConfig.amount}" /></td></tr>
</c:if> 
<tr><td>启用开始时间</td><td><s:textfield name="starttime" id="starttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
<tr><td>启用结束时间</td><td><s:textfield name="endtime" id="endtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>

<tr><td>次数</td><td><input type="text" name="times" value="${youhuiConfig.times}"/>次/

<select name="timesflag"  >
	<option value="1" <s:if test="youhuiConfig.timesflag==1"> selected="selected" </s:if>>天</option>
	<option value="2" <s:if test="youhuiConfig.timesflag==2"> selected="selected" </s:if>>周</option>
	<option value="3" <s:if test="youhuiConfig.timesflag==3"> selected="selected" </s:if>>月</option>
	<option value="4" <s:if test="youhuiConfig.timesflag==4"> selected="selected" </s:if>>年</option>
</select>
</td></tr>

<tr><td>等级</td><td>
<label><input type="checkbox" name="viplevel" value="0" <s:if test="youhuiConfig.vip.contains(\"0\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label>
<label><input type="checkbox" name="viplevel" value="1" <s:if test="youhuiConfig.vip.contains(\"1\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label>
<label><input type="checkbox" name="viplevel" value="2" <s:if test="youhuiConfig.vip.contains(\"2\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label>
<label><input type="checkbox" name="viplevel" value="3" <s:if test="youhuiConfig.vip.contains(\"3\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label>
<label><input type="checkbox" name="viplevel" value="4" <s:if test="youhuiConfig.vip.contains(\"4\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label>
<label><input type="checkbox" name="viplevel" value="5" <s:if test="youhuiConfig.vip.contains(\"5\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label>
<label><input type="checkbox" name="viplevel" value="6" <s:if test="youhuiConfig.vip.contains(\"6\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(6)"/></label>
<label><input type="checkbox" name="viplevel" value="7" <s:if test="youhuiConfig.vip.contains(\"7\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(7)"/></label>
<label><input type="checkbox" name="viplevel" value="8" <s:if test="youhuiConfig.vip.contains(\"8\")">checked="checked"</s:if> ><s:property value="@dfh.model.enums.VipLevel@getText(8)"/></label>
<input type="hidden" id="vip" name="vip"/>
</td></tr>
<tr><td align="center"><input type="button" value="更新" onclick="update();"/></td><td></td></tr>

</table>
	</s:form>
</div>