<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>

<script type="text/javascript">
function create(){
	var str="";
	$("input[name='viplevel']:checked").each(function(){   
	     str+=$(this).val();   
	 });
	$("#vip").val(str);
	if(confirm("确定？")){
		var data = $("#mainform").serialize();
		var action = "/office/insertYouHuiConfig.do";
		var xmlhttp = new Ajax.Request(
				action,
		        {
		            method: 'post',
		            parameters:data+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
	}
}

function responseMethod(data){

	alert(data.responseText);
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
	window.close();
}

function displayTr(value){
	if(value == '自助PT8元优惠'){
		$(".tiyanjin").css("display","block");
		$(".cunsong").css("display","none");
	}else{
		$(".tiyanjin").css("display","none");
		$(".cunsong").css("display","block");
	}
	console.log(value);
}
</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		
		
		<table >
<tr><td>优惠类型</td><td><select name="title" onchange="displayTr(this.value);">
	<option value="生日礼金">生日礼金</option>
</select><span style="color: red;">*</span> </td></tr>
<tr></tr>
<tr><td>标题 </td><td><input type="text" name="aliasTitle" required="required"/><span style="color: red;">*</span></td></tr>
</table>
<table >
<!-- <tr class="cunsong"><td>百分比</td><td><input type="text" name="percent" /><span style="color: red;">*</span></td></tr> -->
<tr></tr>
<tr class="cunsong"><td>流水倍数</td><td><input type="text" name="betMultiples" /><span style="color: red;">*</span></td></tr>
<tr></tr>
<tr class="cunsong"><td>最大额度</td><td><input type="text" name="limitMoney" /><span style="color: red;">*</span></td></tr>
<tr></tr>

<tr class="tiyanjin" style="display: none;"><td>体验金额度</td><td><input name="amount"  /><span style="color: red;">*</span></td></tr>
<tr></tr>
</table>

<table >
<tr><td>启用开始时间</td><td><s:textfield name="starttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
<tr><td>启用结束时间</td><td><s:textfield name="endtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
<tr><td>次数</td><td><input type="text" name="times"/>次/<select name="timesflag"><option value="1">日</option><option value="2">周</option><option value="3">月</option><option value="4">年</option></select></td></tr>
<tr><td>等级</td><td>
<label><input type="checkbox" name="viplevel" value="0" ><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label>
<label><input type="checkbox" name="viplevel" value="1" ><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label>
<label><input type="checkbox" name="viplevel" value="2" ><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label>
<label><input type="checkbox" name="viplevel" value="3" ><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label>
<label><input type="checkbox" name="viplevel" value="4" ><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label>
<label><input type="checkbox" name="viplevel" value="5" ><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label>
<label><input type="checkbox" name="viplevel" value="6" ><s:property value="@dfh.model.enums.VipLevel@getText(6)"/></label>
<label><input type="checkbox" name="viplevel" value="7" ><s:property value="@dfh.model.enums.VipLevel@getText(7)"/></label>
<label><input type="checkbox" name="viplevel" value="8" ><s:property value="@dfh.model.enums.VipLevel@getText(8)"/></label>
<input type="hidden" id="vip" name="vip"/>
</td></tr>
<tr><td align="center"><input type="button" value="创建" onclick="create()"/></td><td></td></tr>

</table>
	</s:form>
</div>